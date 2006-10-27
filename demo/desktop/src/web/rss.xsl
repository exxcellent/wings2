<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

   <xsl:output method="html"
        encoding="UTF-8"
        indent="yes"/>

    <xsl:template match="text()">
    </xsl:template>

    <xsl:template match="/">
        <table style="border:thin solid #f0f0f0">
        <xsl:for-each select="//*[local-name() = 'item']">
        <tr>
            <td>
                <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="*[local-name() = 'link']"/>
                    </xsl:attribute>
                    <xsl:value-of select="*[local-name() = 'title']" disable-output-escaping="yes"/>
                </xsl:element>
             </td>
         </tr>
        </xsl:for-each>
        </table>
    </xsl:template>

</xsl:stylesheet>
