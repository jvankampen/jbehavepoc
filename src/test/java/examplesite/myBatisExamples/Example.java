package examplesite.myBatisExamples;

public class Example {

	 // MyBatis uses interface mappers and annotations to map SQL queries.
	 // Below is an example TestNG @Test that is built off the Xelenium framework.
	 // It is commented out because there is not a database to hit.
	 // 
	 // @Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
	 // public void testDatabaseMapping(EsFace ef) {
	 // 		ef.newSqlXessionFactory("exampleFactory","/src/test/resources/myBatis/myBatis-config-exampleSite.xml");
	 // 		ef.getXessionFactory("exampleFactory").openSession();
	 // 		ExampleMapper mapper = ef.getXessionFactory("exampleFactory").session().getMapper(ExampleMapper.class);
	 // 		Example example = mapper.selectExample(9001);
	 // 		ef.getXessionFactory("exampleFactory").closeSession();
	 // }
	
	public String id;
}
