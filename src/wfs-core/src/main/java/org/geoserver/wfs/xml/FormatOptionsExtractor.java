/* (c) 2026 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.geoserver.ows.kvp.FormatOptionsKvpParser;
import org.geotools.xsd.Node;

/** Static methods for accessing the FormatOptions KVP parser. */
public class FormatOptionsExtractor {

    /** Fully setup KVP parser for formatOptions. Injected by spring at runtime */
    private static FormatOptionsKvpParser wfsFormatOptionsKvpParser = null;

    public static FormatOptionsKvpParser getWfsFormatOptionsKvpParser() {
        return wfsFormatOptionsKvpParser;
    }

    public static void setWfsFormatOptionsKvpParser(FormatOptionsKvpParser wfsFormatOptionsKvpParser) {
        FormatOptionsExtractor.wfsFormatOptionsKvpParser = wfsFormatOptionsKvpParser;
    }

    /**
     * Fix the node object to store a parsed map of formatOptions instead of a raw string. This prevents the parse()
     * method choking later on...
     */
    @SuppressWarnings("unchecked") // no generics in EMF model
    public static void fixNodeObject(Node node) throws Exception {
        Map<String, Object> formatOptions = null;
        if (node.hasAttribute("formatOptions")) {
            Node formatOptionsAttribute = node.getAttribute("formatOptions");
            formatOptions =
                    (Map<String, Object>) wfsFormatOptionsKvpParser.parse((String) formatOptionsAttribute.getValue());

            EList formatOptionsList = new org.eclipse.emf.common.util.BasicEList<>();
            formatOptionsList.addAll(formatOptions.entrySet());

            formatOptionsAttribute.setValue(formatOptions);
        }
    }
}
