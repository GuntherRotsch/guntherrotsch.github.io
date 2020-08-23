<#include "header.ftl">

	<#include "menu.ftl">

	<#if (content.title)??>
	<div class="page-header">
		<h1><#escape x as x?xml>${content.title}</#escape></h1>
	</div>
	<#else></#if>

	<p><em>${content.date?string("dd MMMM yyyy")}</em></p>

  <#if content.summary??>
	<div class="dlist">
   <dl>
    <dt class="hdlist1">
     	Summary
    </dt>
    <dd>
			<p>${content.summary}</p>
    </dd>
   </dl>
  </div>
  </#if>

	<p>${content.body}</p>

  <span>Tags:
	<#list tags as tag>
	  <#list tag.tagged_posts as post>
	    <#assign tagged_post_title = post.title>
			<#if tagged_post_title == content.title>
			  <span><a style="border: 2px solid; padding: 0.3em" href="${content.rootpath}${tag.uri}">${tag.name}</a></span>
			</#if>
	</#list>
	</#list>
	</span>

	<hr />

<#include "footer.ftl">
