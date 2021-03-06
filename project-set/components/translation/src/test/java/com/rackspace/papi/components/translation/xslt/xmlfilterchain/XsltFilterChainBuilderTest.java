package com.rackspace.papi.components.translation.xslt.xmlfilterchain;

import com.rackspace.papi.components.translation.xslt.StyleSheetInfo;
import com.rackspace.papi.components.translation.xslt.XsltParameter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class XsltFilterChainBuilderTest {
    public static class WhenBuildingChains {

        private static SAXTransformerFactory factory;
        private XmlFilterChainBuilder builder;

        @BeforeClass
        public static void before() {
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        }

        @Before
        public void setUp() {
            builder = new XmlFilterChainBuilder(factory);
        }

        @Test
        public void shouldHandleEmptySetOfStyles() {
            XmlFilterChain chain = builder.build();

            assertNotNull("Should build an empty filter chain", chain);
            assertEquals("Should have 0 filter", 0, chain.getFilters().size());
        }

        @Test
        public void shouldHandleStyleSheetList() {
            XmlFilterChain chain = builder.build(new StyleSheetInfo("", "classpath:///style.xsl"));

            assertNotNull("Should build a filter chain", chain);
            assertEquals("Should have 1 filter", 1, chain.getFilters().size());
        }

    }
    
    public static class WhenExecutingChains {
        private static SAXTransformerFactory factory;
        private XmlFilterChainBuilder builder;
        private static final String params = "<params><param name='p1' value='pv1'/><param name='p2' value='pv2'/></params>";
        private static final String headers = "<headers><header name='h1' value='hv1'/><header name='h2' value='hv2'/></headers>";
        private ByteArrayOutputStream headersOutput;
        private ByteArrayOutputStream queryOutput;
        private ByteArrayOutputStream output;
        private ByteArrayInputStream headersInput;
        private ByteArrayInputStream queryInput;
        private InputStream body;

        @BeforeClass
        public static void before() {
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            factory = (SAXTransformerFactory) TransformerFactory.newInstance();
        }

        @Before
        public void setUp() {
            builder = new XmlFilterChainBuilder(factory);
            headersOutput = new ByteArrayOutputStream();
            queryOutput = new ByteArrayOutputStream();
            output = new ByteArrayOutputStream();
            headersInput = new ByteArrayInputStream(headers.getBytes());
            queryInput = new ByteArrayInputStream(params.getBytes());
            body = getClass().getResourceAsStream("/empty.xml");
        }

        @Test
        public void shouldUseInputOutputStreams() {
            List<XsltParameter> inputs = new ArrayList<XsltParameter>();

            inputs.add(new XsltParameter("headers", headersInput));
            inputs.add(new XsltParameter("query", queryInput));

            List<XsltParameter<? extends OutputStream>> outputs = new ArrayList<XsltParameter<? extends OutputStream>>();
            outputs.add(new XsltParameter<OutputStream>("headers.html", headersOutput));
            outputs.add(new XsltParameter<OutputStream>("query.html", queryOutput));
            
            XmlFilterChain chain = builder.build(new StyleSheetInfo("", "classpath:///style.xsl"));
            chain.executeChain(body, output, inputs, outputs);
            
            String headersResult = headersOutput.toString();
            String queryResult = queryOutput.toString();
            String outResult = output.toString();
            
            assertTrue("Should have header output", headersResult.length() > 0);
            assertTrue("Should have query output", queryResult.length() > 0);
            assertTrue("Shoudl have main output", outResult.length() > 0);
            
        }
        
    }
    
}
