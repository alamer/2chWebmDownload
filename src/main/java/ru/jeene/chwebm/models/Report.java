/*
 $Author$
 $Date$
 $Revision$
 $Source$
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jeene.chwebm.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/**
 *
 * @author ivc_ShherbakovIV
 */
@Data
public class Report {

    private List<Model_Report> list;

    public Report() {
        list = Collections.synchronizedList(new ArrayList());
    }

    public void put(Model_Report m) {
        list.add(m);
    }

    public String reportByCount() {
        StringBuilder res = new StringBuilder();
        int cnt_404 = 0;
        int cnt_dl = 0;
        int cnt_cache = 0;
        for (Model_Report model_Report : list) {
            switch (model_Report.getStatus()) {
                case Model_Report.STATUS_DL:
                    cnt_dl++;
                    break;
                case Model_Report.STATUS_CACHED:
                    cnt_cache++;
                    break;
                case Model_Report.STATUS_404:
                    cnt_404++;
                    break;
            }
        }
        res.append("Downloaded: ").append(cnt_dl).append(" ");        
        res.append("Cached: ").append(cnt_cache).append(" ");
        res.append("Not found: ").append(cnt_404).append(" ");
        return res.toString();
    }

}
