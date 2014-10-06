package examplesite.myBatisExamples;

import org.apache.ibatis.annotations.*;

public interface ExampleMapper {
	
	 // MyBatis uses interface mappers and annotations to map SQL queries.
	 // Below is an example TestNG @Test that is built off the Xelenium framework.
	 // It is commented out because there is not a database to hit.
	 // 
	 // @Test(dataProvider = MULTIBROWSER,retryAnalyzer=Retry.class)
     // public void testDatabaseMapping(EsFace ef) {
     // 		ef.newSqlXessionFactory("exampleFactory","/src/test/resources/myBatis/myBatis-config-exampleSite.xml");
     // 		ef.getXessionFactory("exampleFactory").openSession();
     // 		ExampleMapper mapper = ef.getXessionFactory("exampleFactory").getMapper(ExampleMapper.class);
     // 		Example example = mapper.selectExample(9001);
     // 		ef.getXessionFactory("exampleFactory").closeSession();
     // }
	
	@Select("SELECT * FROM example WHERE id = #{id}")
	Example selectExample(int id);
}
