<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Notifications</title>
      </head>
      <body>
      	<h1><u>Notifications</u></h1>
      	<xsl:for-each select="child::notes/child::note">
      		<xsl:variable name="a" select="child::s"/>
      		<xsl:variable name="b" select="child::u"/>
      		<xsl:variable name="c" select="child::d"/>
      		<div class="notification">
  				<abbr class="update-date" title="{$c}"><xsl:value-of select="$c"/></abbr><br/>
  				<a rel="subscription" href="{$a}">Subscribed to User</a><br/>
  				<a rel="update" href="{$b}">Updated Bookmark</a><br/>
			</div>
        </xsl:for-each>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>