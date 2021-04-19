package edu.ncsu.projects.dbms2.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Component;

@Component
public class Membership {
	
	private String membershipLevel;
	private Double durationMonths;
	private Double cashbackRewardPercent;
	
	public String getMembershipLevel() {
		return membershipLevel;
	}
	public void setMembershipLevel(String membershipLevel) {
		this.membershipLevel = membershipLevel;
	}
	public Double getDurationMonths() {
		return durationMonths;
	}
	public void setDurationMonths(Double durationMonths) {
		this.durationMonths = durationMonths;
	}
	public Double getCashbackRewardPercent() {
		return cashbackRewardPercent;
	}
	public void setCashbackRewardPercent(Double cashbackRewardPercent) {
		this.cashbackRewardPercent = cashbackRewardPercent;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
	
}
