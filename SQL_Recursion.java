/*
 * Seung Yoon Oh
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SQL_Recursion
{
	public static void main(String[] args) throws ClassNotFoundException
	{
    // load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try
		{
      // create a database connection

      connection = DriverManager.getConnection("jdbc:sqlite:" + args[0]);
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      statement.execute("PRAGMA foreign_keys = ON;");

      statement.executeUpdate("drop table if exists influence;");
      statement.executeUpdate("drop table if exists delta;");
      statement.executeUpdate("drop table if exists t;");
      statement.executeUpdate("drop table if exists told;");
      statement.executeUpdate("drop table if exists funds;");


    statement.executeUpdate("CREATE TABLE influence ([from] VARCHAR(100), [to] VARCHAR(100),"
    + "FOREIGN KEY ([from]) REFERENCES customer(name) ON DELETE CASCADE,"
    + "FOREIGN KEY ([to]) REFERENCES customer(name) ON DELETE CASCADE);");

      statement.executeUpdate("CREATE TABLE delta ([from] VARCHAR(100), [to] VARCHAR(100),"
    + "FOREIGN KEY ([from]) REFERENCES customer(name) ON DELETE CASCADE,"
    + "FOREIGN KEY ([to]) REFERENCES customer(name) ON DELETE CASCADE);");

      statement.executeUpdate("CREATE TABLE t ([from] VARCHAR(100), [to] VARCHAR(100),"
    + "FOREIGN KEY ([from]) REFERENCES customer(name) ON DELETE CASCADE,"
    + "FOREIGN KEY ([to]) REFERENCES customer(name) ON DELETE CASCADE);");
      
      statement.executeUpdate("CREATE TABLE told ([from] VARCHAR(100), [to] VARCHAR(100),"
    + "FOREIGN KEY ([from]) REFERENCES customer(name) ON DELETE CASCADE,"
    + "FOREIGN KEY ([to]) REFERENCES customer(name) ON DELETE CASCADE);");

      statement.executeUpdate("CREATE TABLE funds ([from] VARCHAR(100), [to] VARCHAR(100),"
    + "FOREIGN KEY ([from]) REFERENCES customer(name) ON DELETE CASCADE,"
    + "FOREIGN KEY ([to]) REFERENCES customer(name) ON DELETE CASCADE);");

    statement.executeUpdate("INSERT INTO t([from],[to])select d.cname, d1.cname from depositor d, depositor d1, transfer t where d.ano = t.src AND d1.ano = t.tgt;" );
    statement.executeUpdate("INSERT INTO delta([from],[to])select d.cname, d1.cname from depositor d, depositor d1, transfer t where d.ano = t.src AND d1.ano = t.tgt;" );
    statement.executeUpdate("INSERT INTO funds([from],[to])select d.cname, d1.cname from depositor d, depositor d1, transfer t where d.ano = t.src AND d1.ano = t.tgt;" );



    ResultSet count = statement.executeQuery("select count(*) from delta;");
      while(count.getInt(1)>0){
        statement.executeUpdate("DELETE from told;");
      	statement.executeUpdate("INSERT INTO told([from],[to])select * from t;"); //Told = T

        statement.executeUpdate("DELETE from t;");
        statement.executeUpdate("INSERT INTO t([from],[to]) select distinct * from told UNION select distinct x.[from], y.[to] from funds x, delta y where x.[to] = y.[from];");

        statement.executeUpdate("DELETE from delta;");
        statement.executeUpdate("INSERT INTO delta select [from],[to] from t except select [from],[to] from told;");
      	count = statement.executeQuery("select count(*) from delta;");
      }

      statement.executeUpdate("DELETE from t where [from] = [to];");
      statement.executeUpdate("INSERT INTO influence([from],[to]) select * from t;");

      statement.executeUpdate("drop table if exists delta;");
      statement.executeUpdate("drop table if exists t;");
      statement.executeUpdate("drop table if exists told;");
      statement.executeUpdate("drop table if exists funds;");

      statement.close();
      count.close();
  }
  catch(SQLException e)
  {
      // if the error message is "out of memory", 
      // it probably means no database file is found
  	System.err.println(e.getMessage());
  }
  finally
  {
  	try
  	{
  		if(connection != null)
  			connection.close();
  	}
  	catch(SQLException e)
  	{
        // connection close failed.
  		System.err.println(e);
  	}
  }
}
}