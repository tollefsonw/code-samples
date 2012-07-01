<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>User Bookmarks</title>
      </head>
      <body>
      	<h1><u>Bookmarks</u></h1>
      	<ul>
      	<xsl:for-each select="child::urls/child::url">
      		<xsl:variable name="uri" select="child::uri"/>
      		<xsl:variable name="bookmark" select="child::bookmark"/>
  			<li><xsl:value-of select="$bookmark"/> <a href="{$uri}" rel="url"> (metadata)</a></li>
        </xsl:for-each>
        </ul>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>