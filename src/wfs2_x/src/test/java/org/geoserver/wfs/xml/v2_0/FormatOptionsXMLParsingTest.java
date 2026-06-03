/* (c) 2026 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.util.Map;
import net.opengis.wfs20.GetFeatureType;
import org.geoserver.wfs.WFSTestSupport;
import org.geoserver.wfs.request.GetFeatureRequest;
import org.geotools.xsd.Parser;
import org.junit.Test;

/**
 * Test to verify that formatOptions attribute is properly parsed from XML POST requests.
 *
 * @author Roar Brænden
 */
public class FormatOptionsXMLParsingTest extends WFSTestSupport {

    @Test
    public void testFormatOptionsAttributeParsing() throws Exception {
        String xml =
                """
                <wfs:GetFeature service="WFS" version="2.0.0"
                    xmlns:wfs="http://www.opengis.net/wfs/2.0"
                    xmlns:fes="http://www.opengis.net/fes/2.0"
                    xmlns:cite="http://www.opengis.net/cite"
                    outputFormat="shape-zip"
                    formatOptions="PRJFILEFORMAT:ESRI;FILENAME:myfile.zip">
                    <wfs:Query typeNames="cite:Buildings">
                        <fes:Filter>
                            <fes:ResourceId rid="Buildings.1"/>
                        </fes:Filter>
                    </wfs:Query>
                </wfs:GetFeature>
                """;

        org.geoserver.wfs.xml.v2_0.WFSConfiguration configuration = new org.geoserver.wfs.xml.v2_0.WFSConfiguration();
        Parser parser = new Parser(configuration);

        GetFeatureType gft = (GetFeatureType) parser.parse(new ByteArrayInputStream(xml.getBytes()));
        assertNotNull("GetFeatureType should not be null", gft);

        GetFeatureRequest request = GetFeatureRequest.adapt(gft);
        assertNotNull("GetFeatureRequest should not be null", request);

        Map<String, Object> formatOptions = request.getFormatOptions();
        assertNotNull("formatOptions should not be null", formatOptions);
        assertEquals("formatOptions should have 3 entries", 3, formatOptions.size());

        assertEquals("PRJFILEFORMAT should be ESRI", "ESRI", formatOptions.get("PRJFILEFORMAT"));
        assertEquals("FILENAME should be myfile.zip", "myfile.zip", formatOptions.get("FILENAME"));
    }

    @Test
    public void testNoFormatOptionsAttribute() throws Exception {
        String xml =
                """
                <wfs:GetFeature service="WFS" version="2.0.0"
                    xmlns:wfs="http://www.opengis.net/wfs/2.0"
                    xmlns:fes="http://www.opengis.net/fes/2.0"
                    xmlns:cite="http://www.opengis.net/cite"
                    outputFormat="application/json">
                    <wfs:Query typeNames="cite:Buildings">
                        <fes:Filter>
                            <fes:ResourceId rid="Buildings.1"/>
                        </fes:Filter>
                    </wfs:Query>
                </wfs:GetFeature>
                """;

        org.geoserver.wfs.xml.v2_0.WFSConfiguration configuration = new org.geoserver.wfs.xml.v2_0.WFSConfiguration();
        Parser parser = new Parser(configuration);

        GetFeatureType gft = (GetFeatureType) parser.parse(new ByteArrayInputStream(xml.getBytes()));
        assertNotNull("GetFeatureType should not be null", gft);

        GetFeatureRequest request = GetFeatureRequest.adapt(gft);
        assertNotNull("GetFeatureRequest should not be null", request);

        Map<String, Object> formatOptions = request.getFormatOptions();
        assertNotNull("formatOptions should not be null even when attribute is not present", formatOptions);
        assertEquals("formatOptions should be empty", 0, formatOptions.size());
    }
}
