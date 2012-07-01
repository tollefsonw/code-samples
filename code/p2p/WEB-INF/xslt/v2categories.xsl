<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Categories</title>
      </head>
      <body>
      	<h1><u>Categories</u></h1>
      	<ul>
      	<xsl:for-each select="child::categories/child::category">
  			<li><a class="category" href="/v2/categories/{current()}"><xsl:value-of select="current()"/></a></li>
        </xsl:for-each>
        </ul>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>
