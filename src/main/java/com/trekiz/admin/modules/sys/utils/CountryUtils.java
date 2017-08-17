package com.trekiz.admin.modules.sys.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.repository.CountryDao;

public class CountryUtils {
	
    private static CountryDao countryDao = SpringContextHolder.getBean(CountryDao.class);
    
    public static final String CACHE_COUNTRY_MAP = "countryMap";
    
    public static Map<Long, Country> getCountryList(){
        Map<Long, Country> countryMap = Maps.newHashMap();
        Iterable<Country> countrys = countryDao.findAll();
        for(Country country:countrys){
            countryMap.put(country.getId(), country);
        }
        return countryMap;
    }
    
    public static Country getCountry(Long countryId){
        return CountryUtils.getCountryList().get(countryId);
    }
    
	public static String getCountryName(Long countryId) {
		if (null == countryId || 0 > countryId) {
			return "";
		}

		Country country = getCountry(countryId);
		if (country == null) {
			return "";
		}

		return country.getCountryName_cn();
	}
    
    public static Country getCountryId(String countryName){
    	return countryDao.getCountryByCountryName(countryName);
    }
    
    public static Map<Long, String> getCountryMap(){
        Map<Long, String> countryMap = Maps.newHashMap();
        Iterable<Country> countrys = countryDao.findAll();
        for(Country country:countrys){
            countryMap.put(country.getId(), country.getCountryName_cn());
        }
        return countryMap;
    }
    
    public static List<Country> getCountrys(){
    	List<Country> countryList = new ArrayList<Country>();
    	Iterable<Country> countrys = countryDao.getCountrys();
    	for(Country country:countrys){
    		countryList.add(country);
    	}
    	return countryList;
    }
    
    public static List<Country> getCountrys(List<String> countryIdList) {
    	StringBuffer ids = new StringBuffer();
    	for(String id : countryIdList) {
    		ids.append(id += ",");
    	}
    	List<Country> countryList = new ArrayList<Country>();
    	Iterable<Country> countrys = countryDao.getCountrys(ids.toString());
        for(Country country:countrys){
            countryList.add(country);
          }
         return countryList;
    }
}
