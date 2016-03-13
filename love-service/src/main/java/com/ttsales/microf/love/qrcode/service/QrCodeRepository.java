package com.ttsales.microf.love.qrcode.service;

import com.ttsales.microf.love.qrcode.domain.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by liyi on 16/3/13.
 */
@RepositoryRestResource
public interface QrCodeRepository extends JpaRepository<QrCode,Long>{

}
