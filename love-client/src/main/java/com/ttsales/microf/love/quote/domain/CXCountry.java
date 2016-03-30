package com.ttsales.microf.love.quote.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liyi on 2016/3/25.
 */
@Data
@Entity
@Table(name = "dat_price_cx_country")
public class CXCountry extends SuperCX{

}
