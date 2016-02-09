/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.dtls.fairdatapoint.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.dtls.fairdatapoint.api.controller.utils.HandleHttpHeadersUtils;
import nl.dtls.fairdatapoint.service.DataAccessorService;
import nl.dtls.fairdatapoint.service.DataAccessorServiceException;
import org.apache.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openrdf.rio.RDFFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rajaram Kaliyaperumal
 * @since 2016-01-07
 * @version 0.1
 */
@RestController
@Api(description = "FDP data accessor")
@RequestMapping(value = "/{catalogID}/{datasetID}/{distributionID:[^.]+}")
public class DataAccessorController {   
    
    private final static Logger LOGGER 
            = LogManager.getLogger(DataAccessorController.class);
    @Autowired
    private DataAccessorService dataAccessorService;
    
    @ApiOperation(value = "FAIR dataset distribution")
    @RequestMapping(produces = {"application/ld+json", "text/turtle", 
                "application/rdf+xml", "text/n3"}, 
            method = RequestMethod.GET)
    public String getDatasetDistribution(@PathVariable final String catalogID,
            @PathVariable final String datasetID, 
            @PathVariable final String distributionID, 
            HttpServletRequest request,
                    HttpServletResponse response) {
        
        LOGGER.info("Request to get dataset's distribution {}", distributionID);
        LOGGER.info("GET : " + request.getRequestURL());
        String responseBody;
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        RDFFormat requesetedContentType = HandleHttpHeadersUtils.
                requestedAcceptHeader(acceptHeader);        
        HandleHttpHeadersUtils.setMandatoryResponseHeader(response);
        if (requesetedContentType == null) {
            responseBody = HandleHttpHeadersUtils.setUnsupportedResponseHeader(response, acceptHeader);               
        }        
        else {      
            try {
                responseBody = dataAccessorService.retrieveDatasetDistribution(catalogID, 
                        datasetID, distributionID, requesetedContentType);
                HandleHttpHeadersUtils.setSuccessResponseHeader(
                        responseBody, response, requesetedContentType);
            } catch (DataAccessorServiceException ex) {
                responseBody = HandleHttpHeadersUtils.setErrorResponseHeader(
                        response, ex);
            }
        }
        return responseBody;
    }
    
}
