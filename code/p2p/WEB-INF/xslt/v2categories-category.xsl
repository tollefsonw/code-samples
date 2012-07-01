<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Category Bookmarks</title>
      </head>
      <body>
      	<h1><u>Category Bookmarks</u></h1>
      	<ul>
      	<xsl:for-each select="child::bookmarks/child::info">
      		<xsl:variable name="a" select="child::num"/>
      		<xsl:variable name="b" select="child::username"/>
      		<xsl:variable name="c" select="child::page"/>
  			<li><xsl:value-of select="$c"/>  <a class="url" href="/v2/users/{$b}/urls/{$a}"> (metadata)</a></li>
        </xsl:for-each>
        </ul>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>