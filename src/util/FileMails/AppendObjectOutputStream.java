package util.FileMails;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AppendObjectOutputStream extends ObjectOutputStream {
	public AppendObjectOutputStream (OutputStream out) throws IOException {
		super(out);
	}
 
	@Override
	protected void writeStreamHeader() throws IOException {
		
		// Reset header
		reset();
	}
}
