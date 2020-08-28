package com.remototech.remototechapi.vos;

import java.util.List;

import lombok.Data;

@Data
public class JobsFilter {

	private List<String> keyWords;
	private List<String> contractTypes;
	private List<String> experienceTypes;

}
