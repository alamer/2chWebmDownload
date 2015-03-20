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

import lombok.Data;

/**
 *
 * @author ivc_ShherbakovIV
 */
@Data
public class Model_Report {

    public static final int STATUS_DL = 1;
    public static final int STATUS_CACHED = 2;
    public static final int STATUS_404 = 3;

    private String thread;
    private int status;

}
