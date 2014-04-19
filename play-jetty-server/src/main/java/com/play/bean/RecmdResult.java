/**
 * Project: play-jetty-server
 * 
 * File Created at 2014-4-11
 * $Id$
 * 
 * Copyright 2010 dianping.com.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Dianping Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with dianping.com.
 */
package com.play.bean;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;


/**
 * TODO Comment of RecmdResult
 * @author yao.ma
 *
 */
public class RecmdResult {
    
    private List<Action> actionList = new ArrayList<Action>();
    
    private String resultTitle;
    
    private int totalPrice;
    
    private int score1;
    
    private int score2;
    
    private int score3;

    public void addAction(Action action){
    	this.actionList.add(action);
    }
    public List<Action> getActionList()
    {
        return actionList;
    }

    public void setActionList(List<Action> actionList)
    {
        this.actionList = actionList;
    }

    public String getResultTitle()
    {
        return resultTitle;
    }

    public void setResultTitle(String resultTitle)
    {
        this.resultTitle = resultTitle;
    }

    public int getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public int getScore1()
    {
        return score1;
    }

    public void setScore1(int score1)
    {
        this.score1 = score1;
    }

    public int getScore2()
    {
        return score2;
    }

    public void setScore2(int score2)
    {
        this.score2 = score2;
    }

    public int getScore3()
    {
        return score3;
    }

    public void setScore3(int score3)
    {
        this.score3 = score3;
    }
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RecmdResult: ");
		for(Action action : actionList){
			sb.append(action.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
