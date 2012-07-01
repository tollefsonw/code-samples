<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Users</title>
      </head>
      <body>
      	<h1><u>Current Users</u></h1>
      	<ul class="users">
      	<xsl:for-each select="child::users/child::user">
  			<li class="user"><a href="/v2/users/{current()}" rel="details"><xsl:value-of select="current()"/></a></li>
        </xsl:for-each>
        </ul>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>
