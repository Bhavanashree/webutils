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

package com.yukthi.webutils.services;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yukthi.webutils.IWebUtilsInternalConstants;
import com.yukthi.webutils.repository.ITrackedEntity;
import com.yukthi.webutils.security.UserDetails;

/**
 * Context user related services
 * @author akiran
 */
@Service
public class UserService
{
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * Fetches current user details from the request
	 * @return Current user details
	 */
	public UserDetails getCurrentUserDetails()
	{
		return (UserDetails)request.getAttribute(IWebUtilsInternalConstants.REQ_ATTR_USER_DETAILS);
	}
	
	/**
	 * Sets the tracked fields during create operation
	 * @param trackedEntity Entity needs to be tracked
	 */
	public void populateTrackingFieldForCreate(ITrackedEntity trackedEntity)
	{
		UserDetails userDetails = getCurrentUserDetails();

		//set date fields
		Date now = new Date();
		
		trackedEntity.setCreatedOn(now);
		trackedEntity.setUpdatedOn(now);
		
		//if user details are not available skip other fields
		if(userDetails == null)
		{
			return;
		}
		
		//set user fields
		long userId = userDetails.getUserId();
		trackedEntity.setCreatedBy(userId);
		trackedEntity.setUpdatedBy(userId);
	}

	/**
	 * Sets the tracked fields during update operation
	 * @param trackedEntity Entity needs to be tracked
	 */
	public void populateTrackingFieldForUpdate(ITrackedEntity trackedEntity)
	{
		UserDetails userDetails = getCurrentUserDetails();

		//set date fields
		Date now = new Date();
		
		trackedEntity.setUpdatedOn(now);
		
		//if user details are not available skip other fields
		if(userDetails == null)
		{
			return;
		}
		
		//set user fields
		long userId = userDetails.getUserId();
		trackedEntity.setUpdatedBy(userId);
	}
}
