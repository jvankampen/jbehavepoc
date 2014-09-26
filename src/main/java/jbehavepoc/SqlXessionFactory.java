package jbehavepoc;

import java.sql.Connection;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.TransactionIsolationLevel;

public class SqlXessionFactory{
	
	private final SqlSessionFactory sqlSessionFactory;
	
	private SqlSession _session;
	
	/**
	 * Instantiates a new instance of SqlXessionFactory that extends SqlSessionFactory to include a library of
	 * SqlSessions and their accessors.
	 * @param config
	 */
	public SqlXessionFactory(Configuration config){
		this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
	}
	public SqlXessionFactory(SqlSessionFactory baseFactory){
		this.sqlSessionFactory = baseFactory;
	}
	
	/**
	 * Method to get the SqlSession that is part of this SqlXessionFactory.
	 * @return the SqlSession that is part of this SqlXessionFactory
	 */
	public SqlSession session(){
		if(_session == null){
			_session = openSession();
		}
		return _session;
	}
	
	/**
	 * Method to close the SqlSession
	 */
	public void closeSession(){
		_session.close();
	}
	
	
	private SqlSession openSession() {
	    return sqlSessionFactory.openSession();
	}

	@SuppressWarnings("unused")
	private SqlSession openSession(boolean autoCommit) {
		return sqlSessionFactory.openSession(autoCommit);
	}
	
	@SuppressWarnings("unused")
	private SqlSession openSession(ExecutorType execType) {
		return sqlSessionFactory.openSession(execType);
	}
	
	@SuppressWarnings("unused")
	private SqlSession openSession(TransactionIsolationLevel level) {
		return sqlSessionFactory.openSession(level);
	}
	
	@SuppressWarnings("unused")
	private SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
		return sqlSessionFactory.openSession(execType, level);
	}
	
	@SuppressWarnings("unused")
	private SqlSession openSession(ExecutorType execType, boolean autoCommit) {
	    return sqlSessionFactory.openSession(execType, autoCommit);
	}
	
	@SuppressWarnings("unused")
	private SqlSession openSession(Connection connection) {
	    return sqlSessionFactory.openSession(connection);
	}
	
	@SuppressWarnings({"unused" })
	private SqlSession openSession(ExecutorType execType, Connection connection) {
	    return sqlSessionFactory.openSession(execType, connection);
	}
	
	public Configuration getConfiguration() {
	    return sqlSessionFactory.getConfiguration();
	  }
	
	public <T> T getMapper(Class<T> mapper){
		return session().getMapper(mapper);
	}
}
