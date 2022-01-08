<#include "header.ftl">

	<#include "menu.ftl">

	<div class="page-header">
		<h1>Tags</h1>
	</div>

    <ul>
        <#list alltags?sequence?sort as tag>
            <a href="${tag}.html">
                <h4>${tag}</h4>
            </a>
        </#list>
	</ul>

	<hr />

<#include "footer.ftl">
