package com.example.nikhilg.inttesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nikhilg on 8/26/2016.
 */
public class Mobilepayment_ServiceProviderID {

    HashMap<String, Integer> branchListMap = new HashMap<String, Integer>();
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> branchList  = new ArrayList<String>();


    public void storeBranchList(String psKey ,Map<String, List<String>> branchListDetail){
        map = branchListDetail;
    }

    public List<String> getBranchList(String branchName) {
//        if(!branchName.equals(""))
//        {
//        return branchListMap.get(branchName);}
//        return "";

        return map.get(branchName);

    }
    public String getFavoriteName(String branchName) {
//        if(!branchName.equals(""))
//        {
//        return branchListMap.get(branchName);}
//        return "";

        return map.get(branchName).get(0);

    }
    public String getFavoriteMobileNumber(String branchName) {
//        if(!branchName.equals(""))
//        {
//        return branchListMap.get(branchName);}
//        return "";

        return map.get(branchName).get(1);

    }
    public String getCustomerFavoriteID(String branchName)
    {
        return map.get(branchName).get(2);
    }
    public String getRelationshipID(String branchName)
    {
        return map.get(branchName).get(3);
    }


    public void storeBranchListDetail(String psKey ,List<String> branchListDetail){
        branchList = branchListDetail;
    }

    public List<String> getBranchListDetail() {
        return branchList;

    }
}
