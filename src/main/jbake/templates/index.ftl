<#include "header.ftl">

	<#include "menu.ftl">

	<div class="page-header">
		<h1>Blog</h1>
	</div>
	<#list posts as post>
  		<#if (post.status == "published")>
  			<a href="/${post.uri}"><h1><#escape x as x?xml>${post.title}</#escape></h1></a>
  			<p>${post.date?string("dd MMMM yyyy")}</p>
				<#if (post.summary??)>
  				<p>${post.summary}</p>
					<p>
					  <a href="/${post.uri}">Read More...</a>
					</p>
				<#else>
				  <p>${post.body}</p>
				</#if>
  		</#if>
  	</#list>

	<hr />

	<p>Older posts are available in the <a href="${content.rootpath}${config.archive_file}">archive</a>.</p>

<#include "footer.ftl">
