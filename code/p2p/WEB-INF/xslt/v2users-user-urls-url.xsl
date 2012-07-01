<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Meta Data</title>
      </head>
      <body>
      <h1><u>Bookmark Metadata</u></h1>
      <xsl:variable name="page" select="child::meta/child::bookmark"/>
      <xsl:variable name="day" select="child::meta/child::date"/>
      	<div class="url">
      		<p><abbr class="date-added" title="{$day}">Added on: <xsl:value-of select="$day"/></abbr></p>
      		<p>Visit Page: <a rel="source" href="{$page}"><xsl:value-of select="$page"/></a></p>
		</div>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>