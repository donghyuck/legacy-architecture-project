package architecture.ee.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

public class ImageView implements View {

	protected static final int DEFAULT_BUFFER_SIZE = 4096;
	
	private byte imageData[];
	
	private String contentType;


	public ImageView(byte[] imageData, String contentType) {
		super();
		this.imageData = imageData;
		this.contentType = contentType;
	}

	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setContentType(contentType);
		response.getOutputStream().write(imageData);
		
	}
	
	public String getContentType() {
		return contentType;
	}
    
}
