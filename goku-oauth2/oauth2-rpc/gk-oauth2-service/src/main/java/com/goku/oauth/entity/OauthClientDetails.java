package com.goku.oauth.entity;

import java.io.Serializable;
import java.util.Date;

public class OauthClientDetails implements Serializable {
    private static final long serialVersionUID = 417423156278069704L;

    private String clientId;

    /**
     * 客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: "unity-resource,mobile-resource". <br>
	 * 该字段的值必须来源于与security.xml中标签‹oauth2:resource-server的属性resource-id值一致. 在security.xml配置有几个‹oauth2:resource-server标签, 则该字段可以使用几个该值. <br>
	 * 在实际应用中, 我们一般将资源进行分类,并分别配置对应的‹oauth2:resource-server,如订单资源配置一个‹oauth2:resource-server, 用户资源又配置一个‹oauth2:resource-server. 当注册客户端时,根据实际需要可选择资源id,也可根据不同的注册流程,赋予对应的资源id.
     */
    private String resourceIds;

    /**
     * 用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成). <br>
	 * 对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appSecret,与client_secret是同一个概念.
     */
    private String clientSecret;

    /**
     * 指定客户端申请的权限范围,可选值包括read,write,trust;若有多个权限范围用逗号(,)分隔,如: "read,write". <br>
	 * scope的值与security.xml中配置的‹intercept-url的access属性有关系. 如‹intercept-url的配置为<br>
	 * ‹intercept-url pattern="/m/**" access="ROLE_MOBILE,SCOPE_READ"/><br>
	 * 则说明访问该URL时的客户端必须有read权限范围. write的配置值为SCOPE_WRITE, trust的配置值为SCOPE_TRUST. <br>
	 * 在实际应该中, 该值一般由服务端指定, 常用的值为read,write
     */
    private String scope;

    /**
     * 授权方式：<br>
	 * 1.authorization_code                      授权码模式(即先登录获取code,再获取token) <br>
	 * 2.password                                     密码模式(将用户名,密码传过去,直接获取token)<br>
	 * 3.refresh_token                            刷新token<br>
	 * 4.implicit                                        简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)<br>
	 * 指定客户端支持的grant_type,可选值包括authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔,如: "authorization_code,password". <br>
	 * 在实际应用中,当注册时,该字段是一般由服务器端指定的,而不是由申请者去选择的,最常用的grant_type组合有: "authorization_code,refresh_token"(针对通过浏览器访问的客户端); "password,refresh_token"(针对移动设备的客户端). <br>
	 * implicit与client_credentials在实际中很少使用.<br>
	 * 
     */
    private String authorizedGrantTypes;

    /**
     * 客户端的重定向URI,可为空, 当grant_type为authorization_code或implicit时, 在Oauth的流程中会使用并检查与注册时填写的redirect_uri是否一致. 下面分别说明:<br>
	 * 当grant_type=authorization_code时, 第一步 从 spring-oauth-server获取 'code'时客户端发起请求时必须有redirect_uri参数, 该参数的值必须与 web_server_redirect_uri的值一致. 第二步 用 'code' 换取 'access_token' 时客户也必须传递相同的redirect_uri. <br>
	 * 在实际应用中, web_server_redirect_uri在注册时是必须填写的, 一般用来处理服务器返回的code, 验证state是否合法与通过code去换取access_token值. <br>
	 * 在spring-oauth-client项目中, 可具体参考AuthorizationCodeController.java中的authorizationCodeCallback方法.<br>
	 * 当grant_type=implicit时通过redirect_uri的hash值来传递access_token值.如:<br>
	 * http://localhost:7777/spring-oauth-client/implicit#access_token=dc891f4a-ac88-4ba6-8224-a2497e013865&token_type=bearer&expires_in=43199<br>
	 * 然后客户端通过JS
     */
    private String webServerRedirectUri;

    /**
     * 指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如: "ROLE_UNITY,ROLE_USER". <br>
	 * 对于是否要设置该字段的值,要根据不同的grant_type来判断, 若客户端在Oauth流程中需要用户的用户名(username)与密码(password)的(authorization_code,password), <br>
	 * 则该字段可以不需要设置值,因为服务端将根据用户在服务端所拥有的权限来判断是否有权限访问对应的API. <br>
	 * 但如果客户端在Oauth流程中不需要用户信息的(implicit,client_credentials), <br>
	 * 则该字段必须要设置对应的权限值, 因为服务端将根据该字段值的权限来判断是否有权限访问对应的API. 
     */
    private String authorities;

    /**
     * 设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). <br>
	 * 在服务端获取的access_token JSON数据中的expires_in字段的值即为当前access_token的有效时间值. <br>
	 * 在项目中, 可具体参考DefaultTokenServices.java中属性accessTokenValiditySeconds. <br>
	 * 在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义.
     */
    private Integer accessTokenValidity;

    /**
     * 设定客户端的refresh_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天). <br>
	 * 若客户端的grant_type不包括refresh_token,则不用关心该字段 在项目中, 可具体参考DefaultTokenServices.java中属性refreshTokenValiditySeconds. <br>
	 * <br>
	 * 在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义.
     */
    private Integer refreshTokenValidity;

    /**
     * 这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:
     */
    private String additionalInformation;

    private Date createTime;

    /**
     * 用于标识客户端是否已存档(即实现逻辑删除),默认值为'0'(即未存档). <br>
	 * 对该字段的具体使用请参考CustomJdbcClientDetailsService.java,在该类中,扩展了在查询client_details的SQL加上archived = 0条件 (扩展字段)
     */
    private Boolean archived;

    /**
     * 设置客户端是否为受信任的,默认为'0'(即不受信任的,1为受信任的). <br>
	 * 该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为0,则会跳转到让用户Approve的页面让用户同意授权, <br>
	 * 若该字段为1,则在登录后不需要再让用户Approve同意授权(因为是受信任的). <br>
	 * 对该字段的具体使用请参考OauthUserApprovalHandler.java. (扩展字段)
     */
    private Boolean trusted;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return 客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: "unity-resource,mobile-resource". <br>
	 *         该字段的值必须来源于与security.xml中标签‹oauth2:resource-server的属性resource-id值一致. 在security.xml配置有几个‹oauth2:resource-server标签, 则该字段可以使用几个该值. <br>
	 *         在实际应用中, 我们一般将资源进行分类,并分别配置对应的‹oauth2:resource-server,如订单资源配置一个‹oauth2:resource-server, 用户资源又配置一个‹oauth2:resource-server. 当注册客户端时,根据实际需要可选择资源id,也可根据不同的注册流程,赋予对应的资源id.
     */
    public String getResourceIds() {
        return resourceIds;
    }

    /**
     * @param resourceIds 
	 *            客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: "unity-resource,mobile-resource". <br>
	 *            该字段的值必须来源于与security.xml中标签‹oauth2:resource-server的属性resource-id值一致. 在security.xml配置有几个‹oauth2:resource-server标签, 则该字段可以使用几个该值. <br>
	 *            在实际应用中, 我们一般将资源进行分类,并分别配置对应的‹oauth2:resource-server,如订单资源配置一个‹oauth2:resource-server, 用户资源又配置一个‹oauth2:resource-server. 当注册客户端时,根据实际需要可选择资源id,也可根据不同的注册流程,赋予对应的资源id.
     */
    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    /**
     * @return 用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成). <br>
	 *         对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appSecret,与client_secret是同一个概念.
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @param clientSecret 
	 *            用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成). <br>
	 *            对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appSecret,与client_secret是同一个概念.
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * @return 指定客户端申请的权限范围,可选值包括read,write,trust;若有多个权限范围用逗号(,)分隔,如: "read,write". <br>
	 *         scope的值与security.xml中配置的‹intercept-url的access属性有关系. 如‹intercept-url的配置为<br>
	 *         ‹intercept-url pattern="/m/**" access="ROLE_MOBILE,SCOPE_READ"/><br>
	 *         则说明访问该URL时的客户端必须有read权限范围. write的配置值为SCOPE_WRITE, trust的配置值为SCOPE_TRUST. <br>
	 *         在实际应该中, 该值一般由服务端指定, 常用的值为read,write
     */
    public String getScope() {
        return scope;
    }

    /**
     * @param scope 
	 *            指定客户端申请的权限范围,可选值包括read,write,trust;若有多个权限范围用逗号(,)分隔,如: "read,write". <br>
	 *            scope的值与security.xml中配置的‹intercept-url的access属性有关系. 如‹intercept-url的配置为<br>
	 *            ‹intercept-url pattern="/m/**" access="ROLE_MOBILE,SCOPE_READ"/><br>
	 *            则说明访问该URL时的客户端必须有read权限范围. write的配置值为SCOPE_WRITE, trust的配置值为SCOPE_TRUST. <br>
	 *            在实际应该中, 该值一般由服务端指定, 常用的值为read,write
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * @return 授权方式：<br>
	 *         1.authorization_code                      授权码模式(即先登录获取code,再获取token) <br>
	 *         2.password                                     密码模式(将用户名,密码传过去,直接获取token)<br>
	 *         3.refresh_token                            刷新token<br>
	 *         4.implicit                                        简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)<br>
	 *         指定客户端支持的grant_type,可选值包括authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔,如: "authorization_code,password". <br>
	 *         在实际应用中,当注册时,该字段是一般由服务器端指定的,而不是由申请者去选择的,最常用的grant_type组合有: "authorization_code,refresh_token"(针对通过浏览器访问的客户端); "password,refresh_token"(针对移动设备的客户端). <br>
	 *         implicit与client_credentials在实际中很少使用.<br>
	 *         
     */
    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    /**
     * @param authorizedGrantTypes 
	 *            授权方式：<br>
	 *            1.authorization_code                      授权码模式(即先登录获取code,再获取token) <br>
	 *            2.password                                     密码模式(将用户名,密码传过去,直接获取token)<br>
	 *            3.refresh_token                            刷新token<br>
	 *            4.implicit                                        简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)<br>
	 *            指定客户端支持的grant_type,可选值包括authorization_code,password,refresh_token,implicit,client_credentials, 若支持多个grant_type用逗号(,)分隔,如: "authorization_code,password". <br>
	 *            在实际应用中,当注册时,该字段是一般由服务器端指定的,而不是由申请者去选择的,最常用的grant_type组合有: "authorization_code,refresh_token"(针对通过浏览器访问的客户端); "password,refresh_token"(针对移动设备的客户端). <br>
	 *            implicit与client_credentials在实际中很少使用.<br>
	 *            
     */
    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    /**
     * @return 客户端的重定向URI,可为空, 当grant_type为authorization_code或implicit时, 在Oauth的流程中会使用并检查与注册时填写的redirect_uri是否一致. 下面分别说明:<br>
	 *         当grant_type=authorization_code时, 第一步 从 spring-oauth-server获取 'code'时客户端发起请求时必须有redirect_uri参数, 该参数的值必须与 web_server_redirect_uri的值一致. 第二步 用 'code' 换取 'access_token' 时客户也必须传递相同的redirect_uri. <br>
	 *         在实际应用中, web_server_redirect_uri在注册时是必须填写的, 一般用来处理服务器返回的code, 验证state是否合法与通过code去换取access_token值. <br>
	 *         在spring-oauth-client项目中, 可具体参考AuthorizationCodeController.java中的authorizationCodeCallback方法.<br>
	 *         当grant_type=implicit时通过redirect_uri的hash值来传递access_token值.如:<br>
	 *         http://localhost:7777/spring-oauth-client/implicit#access_token=dc891f4a-ac88-4ba6-8224-a2497e013865&token_type=bearer&expires_in=43199<br>
	 *         然后客户端通过JS
     */
    public String getWebServerRedirectUri() {
        return webServerRedirectUri;
    }

    /**
     * @param webServerRedirectUri 
	 *            客户端的重定向URI,可为空, 当grant_type为authorization_code或implicit时, 在Oauth的流程中会使用并检查与注册时填写的redirect_uri是否一致. 下面分别说明:<br>
	 *            当grant_type=authorization_code时, 第一步 从 spring-oauth-server获取 'code'时客户端发起请求时必须有redirect_uri参数, 该参数的值必须与 web_server_redirect_uri的值一致. 第二步 用 'code' 换取 'access_token' 时客户也必须传递相同的redirect_uri. <br>
	 *            在实际应用中, web_server_redirect_uri在注册时是必须填写的, 一般用来处理服务器返回的code, 验证state是否合法与通过code去换取access_token值. <br>
	 *            在spring-oauth-client项目中, 可具体参考AuthorizationCodeController.java中的authorizationCodeCallback方法.<br>
	 *            当grant_type=implicit时通过redirect_uri的hash值来传递access_token值.如:<br>
	 *            http://localhost:7777/spring-oauth-client/implicit#access_token=dc891f4a-ac88-4ba6-8224-a2497e013865&token_type=bearer&expires_in=43199<br>
	 *            然后客户端通过JS
     */
    public void setWebServerRedirectUri(String webServerRedirectUri) {
        this.webServerRedirectUri = webServerRedirectUri;
    }

    /**
     * @return 指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如: "ROLE_UNITY,ROLE_USER". <br>
	 *         对于是否要设置该字段的值,要根据不同的grant_type来判断, 若客户端在Oauth流程中需要用户的用户名(username)与密码(password)的(authorization_code,password), <br>
	 *         则该字段可以不需要设置值,因为服务端将根据用户在服务端所拥有的权限来判断是否有权限访问对应的API. <br>
	 *         但如果客户端在Oauth流程中不需要用户信息的(implicit,client_credentials), <br>
	 *         则该字段必须要设置对应的权限值, 因为服务端将根据该字段值的权限来判断是否有权限访问对应的API. 
     */
    public String getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities 
	 *            指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如: "ROLE_UNITY,ROLE_USER". <br>
	 *            对于是否要设置该字段的值,要根据不同的grant_type来判断, 若客户端在Oauth流程中需要用户的用户名(username)与密码(password)的(authorization_code,password), <br>
	 *            则该字段可以不需要设置值,因为服务端将根据用户在服务端所拥有的权限来判断是否有权限访问对应的API. <br>
	 *            但如果客户端在Oauth流程中不需要用户信息的(implicit,client_credentials), <br>
	 *            则该字段必须要设置对应的权限值, 因为服务端将根据该字段值的权限来判断是否有权限访问对应的API. 
     */
    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    /**
     * @return 设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). <br>
	 *         在服务端获取的access_token JSON数据中的expires_in字段的值即为当前access_token的有效时间值. <br>
	 *         在项目中, 可具体参考DefaultTokenServices.java中属性accessTokenValiditySeconds. <br>
	 *         在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义.
     */
    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    /**
     * @param accessTokenValidity 
	 *            设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). <br>
	 *            在服务端获取的access_token JSON数据中的expires_in字段的值即为当前access_token的有效时间值. <br>
	 *            在项目中, 可具体参考DefaultTokenServices.java中属性accessTokenValiditySeconds. <br>
	 *            在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义.
     */
    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    /**
     * @return 设定客户端的refresh_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天). <br>
	 *         若客户端的grant_type不包括refresh_token,则不用关心该字段 在项目中, 可具体参考DefaultTokenServices.java中属性refreshTokenValiditySeconds. <br>
	 *         <br>
	 *         在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义.
     */
    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    /**
     * @param refreshTokenValidity 
	 *            设定客户端的refresh_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天). <br>
	 *            若客户端的grant_type不包括refresh_token,则不用关心该字段 在项目中, 可具体参考DefaultTokenServices.java中属性refreshTokenValiditySeconds. <br>
	 *            <br>
	 *            在实际应用中, 该值一般是由服务端处理的, 不需要客户端自定义.
     */
    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    /**
     * @return 这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * @param additionalInformation 
	 *            这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:
     */
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return 用于标识客户端是否已存档(即实现逻辑删除),默认值为'0'(即未存档). <br>
	 *         对该字段的具体使用请参考CustomJdbcClientDetailsService.java,在该类中,扩展了在查询client_details的SQL加上archived = 0条件 (扩展字段)
     */
    public Boolean getArchived() {
        return archived;
    }

    /**
     * @param archived 
	 *            用于标识客户端是否已存档(即实现逻辑删除),默认值为'0'(即未存档). <br>
	 *            对该字段的具体使用请参考CustomJdbcClientDetailsService.java,在该类中,扩展了在查询client_details的SQL加上archived = 0条件 (扩展字段)
     */
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    /**
     * @return 设置客户端是否为受信任的,默认为'0'(即不受信任的,1为受信任的). <br>
	 *         该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为0,则会跳转到让用户Approve的页面让用户同意授权, <br>
	 *         若该字段为1,则在登录后不需要再让用户Approve同意授权(因为是受信任的). <br>
	 *         对该字段的具体使用请参考OauthUserApprovalHandler.java. (扩展字段)
     */
    public Boolean getTrusted() {
        return trusted;
    }

    /**
     * @param trusted 
	 *            设置客户端是否为受信任的,默认为'0'(即不受信任的,1为受信任的). <br>
	 *            该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为0,则会跳转到让用户Approve的页面让用户同意授权, <br>
	 *            若该字段为1,则在登录后不需要再让用户Approve同意授权(因为是受信任的). <br>
	 *            对该字段的具体使用请参考OauthUserApprovalHandler.java. (扩展字段)
     */
    public void setTrusted(Boolean trusted) {
        this.trusted = trusted;
    }
}