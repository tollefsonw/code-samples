<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>User Detail</title>
      </head>
      <body>
      <h1><u>User Detail</u></h1>
      <xsl:variable name="username" select="child::user/child::username"/>
      <xsl:variable name="url" select="child::user/child::url"/>
      	<div class="user">
  			<p><a href="mailto:{$username}" rel="email">Send Email: <xsl:value-of select="$username"/></a></p>
  			<p><a href="{$url}" rel="node">Visit Node</a></p>
 			<p><a href="/v2/users/{$username}/urls" rel="urls">Url's</a></p>
  			<p><a href="/v2/users/{$username}/notifications" rel="notifications">Notifications</a></p>
		</div>
      </body>
    </html>
  </xsl:template>
  
</xsl:stylesheet>
