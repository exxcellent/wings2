<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:java="http://xml.apache.org/xalan/java"
    exclude-result-prefixes="java">

    <xsl:output method="html"
        encoding="UTF-8"
        indent="yes"
        xalan:indent-amount="4"/>

    <xsl:template match="text()">
    </xsl:template>

    <xsl:template match="/rss/channel">
        <table style="border:thin solid #f0f0f0">
            <xsl:apply-templates/>
        </table>
    </xsl:template>

    <xsl:template match="/rdf">
        <table style="border:thin solid #f0f0f0">
            <xsl:apply-templates/>
        </table>
    </xsl:template>

    <xsl:template match="item">
        <tr>
            <td>
                <b><xsl:value-of select="title/text()" disable-output-escaping="yes"/></b>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
