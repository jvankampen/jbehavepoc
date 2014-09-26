package xelenium;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

public class XeleniumException extends RuntimeException{
	
	private static final long serialVersionUID = 2205232068823077594L;
	
	private final Throwable thrown;
	
	/**
	 * XeleniumException can be used to throw exceptions such as IOException without requiring a throws declaration.
	 * @param thrown the exception or error that was thrown
	 */
	public XeleniumException(Throwable thrown, TestFace<?,?> tf) {
		super();
		this.thrown = thrown;
                if(tf != null){
                    tf.log("EXCEPTION: "+getMessage()+". STACK TRACE: "+printStackTraceToString());
                }else{
                    Reporter.log("EXCEPTION: "+getMessage()+". STACK TRACE: "+printStackTraceToString(), true);
                }
	}
        public XeleniumException(Throwable thrown){
            this(thrown, null);
        }
	
	@Override
	public void printStackTrace(PrintStream stream){
		this.thrown.printStackTrace(stream);
	}
	@Override
	public void printStackTrace(){
		this.thrown.printStackTrace();
	}
	
	@Override
	public String getMessage(){
		return this.thrown.getMessage();
	}
	
	public String printStackTraceToString(){
		StringWriter sw = new StringWriter();
		this.thrown.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	@Override
	public Throwable fillInStackTrace(){
		return thrown;
	}
	
	
	
}
