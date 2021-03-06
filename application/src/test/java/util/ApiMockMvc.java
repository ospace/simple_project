package util;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

//@Component
public class ApiMockMvc {
	@Autowired
	private MockMvc mvc;
	
//	@Autowired
//	private ObjectFactory<LccMockMvc> mockMvcFactory;
	
	public static ApiMockMvc create() {
		return new ApiMockMvc();
		//return mockMvcFactory.getObject();
	}
	
	public ApiMockRes performSafe(String url, String content) {
		return performSafe(url, content, "");
	}
	
	public ApiMockRes performSafe(String url, String content, Integer expectedStatus) {
		return performSafe(url, content, "", expectedStatus);
	}
	
	public ApiMockRes performSafe(String url, String content, String sessionId) {
		MvcResult result = sendPost(url, content, sessionId, status().isOk(), is(0));
		return new ApiMockRes(result);
	}
	
	public ApiMockRes performSafe(String url, String content, String sessionId, Integer expectedStatus) {
		MvcResult result = sendPost(url, content, sessionId, status().isOk(), is(expectedStatus));
		return new ApiMockRes(result);
	}
	
	public ApiMockRes performSafe(String url, String content, String sessionId, ResultMatcher action, Integer expectedStatus) {
		MvcResult result = sendPost(url, content, sessionId, action, is(expectedStatus));
		return new ApiMockRes(result);
	}
	
	public void perform(String url, ResultMatcher action, Matcher<Integer> expectedStatus) {
		sendPost(url, "", "", action, expectedStatus);
	}
	
	private MvcResult sendPost(String url, String content, String sessionId, ResultMatcher action, Matcher<Integer> expectedStatus) {
		try {
			return mvc.perform(
					post(url)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.header("sessionId", sessionId)
					.content(content))
			   .andExpect(action)
			   .andExpect(jsonPath("$.status", expectedStatus))
			   .andReturn();
		} catch (Exception e) {
			throw new RuntimeException("sendPost", e);
		}
	}
}
