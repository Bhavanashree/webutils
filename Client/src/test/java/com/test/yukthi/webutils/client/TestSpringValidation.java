/*
 * The MIT License (MIT)
 * Copyright (c) 2015 "Yukthi Techsoft Pvt. Ltd." (http://yukthi-tech.co.in)

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.test.yukthi.webutils.client;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.test.yukthi.webutils.models.TestBean;
import com.yukthi.utils.rest.PostRestRequest;
import com.yukthi.utils.rest.RestClient;
import com.yukthi.utils.rest.RestResult;
import com.yukthi.webutils.common.ICommonConstants;
import com.yukthi.webutils.common.models.BaseResponse;

/**
 * Ensures spring validation is enabled using test controller and test bean
 * @author akiran
 */
public class TestSpringValidation extends TFBase
{
	private RestClient client ;
	
	@BeforeClass
	private void setup()
	{
		client = new RestClient(super.baseUrl);		
	}
	
	private void test(TestBean bean, int expectedCode, String messageSubstr)
	{
		//check for negative test case, where validation fails
		PostRestRequest req = new PostRestRequest("/test/test");
		req.setJsonBody(bean);
		
		RestResult<BaseResponse> result = client.invokeJsonRequest(req, BaseResponse.class);
		Assert.assertEquals(result.getValue().getCode(), expectedCode);
		
		if(messageSubstr != null)
		{
			Assert.assertTrue(result.getValue().getMessage().toLowerCase().contains(messageSubstr.toLowerCase()));
		}
	}
	
	/**
	 * Tests that spring validation is working without any problem
	 */
	@Test
	public void testSpringValidation()
	{
		//check for negative test case, where validation fails
		test(new TestBean(null, 25, "test", "test"), ICommonConstants.RESPONSE_CODE_INVALID_REQUEST, "name");
		test(new TestBean("name", 13, "test", "test"), ICommonConstants.RESPONSE_CODE_INVALID_REQUEST, "age");
		test(new TestBean("name", 25, "test", "mismatch"), ICommonConstants.RESPONSE_CODE_INVALID_REQUEST, "confirmPassword");
		
		//test for positive test case where validation succeeds
		test(new TestBean("name", 25, "test", "test"), ICommonConstants.RESPONSE_CODE_SUCCESS, "name");
	}
}