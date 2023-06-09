package worker;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.sql.*;
import org.json.JSONObject;

class Worker {

  private static final String REDIS_HOST = System.getenv("REDIS_HOST");
  private static final String POSTGRES_HOST = System.getenv("POSTGRES_HOST");
  private static final String POSTGRES_USER = System.getenv("POSTGRES_USER");
  private static final String POSTGRES_PASSWORD = System.getenv("POSTGRES_PASSWORD");

  public static void main(String[] args) {

    try {
      Jedis redis = connectToRedis(REDIS_HOST);
      Connection dbConn = connectToDB(POSTGRES_HOST);

      System.err.println("Watching vote queue");

      while (true) {
        String voteJSON = redis.blpop(0, "votes").get(1);
        JSONObject voteData = new JSONObject(voteJSON);
        String voterID = voteData.getString("voter_id");
        String vote = voteData.getString("vote");

        System.err.printf("Processing vote for '%s' by '%s'\n", vote, voterID);
        createOrUpdateVote(dbConn, voterID, vote);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  static void createOrUpdateVote(Connection dbConn, String voterId, String vote) throws SQLException {
    if (voteExists(dbConn, voterId)) {
      updateVote(dbConn, voterId,  vote);
    } else {
      createVote(dbConn, voterId, vote);
    }

  }

  static boolean voteExists(Connection dbConn, String voterId) throws SQLException {
    PreparedStatement exists = dbConn.prepareStatement("SELECT * FROM votes WHERE id = ?");

    exists.setString(1, voterId);
    ResultSet resultSet = exists.executeQuery();
    return resultSet.next();
  }

  static void createVote(Connection dbConn, String voterID, String vote) throws SQLException {
    PreparedStatement insert = dbConn.prepareStatement(
            "INSERT INTO votes (id, vote) VALUES (?, ?)");
    insert.setString(1, voterID);
    insert.setString(2, vote);
    insert.executeUpdate();


  }
  static void updateVote(Connection dbConn, String voterID, String vote) throws SQLException {
      PreparedStatement update = dbConn.prepareStatement(
        "UPDATE votes SET vote = ? WHERE id = ?");
      update.setString(1, vote);
      update.setString(2, voterID);
      update.executeUpdate();
  }

  static Jedis connectToRedis(String host) {
    Jedis conn = new Jedis(host);

    while (true) {
      try {
        conn.keys("*");
        break;
      } catch (JedisConnectionException e) {
        System.err.println("Waiting for redis");
        sleep(1000);
      }
    }

    System.err.println("Connected to redis");
    return conn;
  }

  static Connection connectToDB(String host) throws SQLException {
    Connection conn = null;

    try {

      Class.forName("org.postgresql.Driver");
      String url = "jdbc:postgresql://" + host + "/postgres";

      while (conn == null) {
        try {
          System.out.println("Connecting user " + POSTGRES_USER + ":" + POSTGRES_PASSWORD + " to DB");
          conn = DriverManager.getConnection(url, POSTGRES_USER, POSTGRES_PASSWORD);
        } catch (SQLException e) {
          System.err.println("Waiting for db");
          sleep(1000);
        }
      }

      PreparedStatement st = conn.prepareStatement(
        "CREATE TABLE IF NOT EXISTS votes (id VARCHAR(255) NOT NULL UNIQUE, vote VARCHAR(255) NOT NULL)");
      st.executeUpdate();

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    System.err.println("Connected to db");
    return conn;
  }

  static void sleep(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      System.exit(1);
    }
  }
}
