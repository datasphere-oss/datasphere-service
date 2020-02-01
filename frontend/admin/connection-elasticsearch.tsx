import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div ng-controller=\"ElasticSearchConnectionController\" className=\"h100 vertical-flex\">
    +             <div className=\"flex\">
    +                 <div className=\"fh oa\">
    +                     <div className=\"h100 small-lr-padding\">
    +                         <div className=\"generic-white-box\">
    +                             <div include-no-scope=\"/templates/admin/fragments/connection-name.html\">
    +                                 <div block-api-error=\"\">
    +                                     <form
    +                                         name=\"connectionParamsForm\"
    +                                         className=\"dkuform-horizontal\"
    +                                         on-smart-change=\"testConnection()\">
    +                                         <h2 className=\"settings-section-title\">连接</h2>
    +                                         <div
    +                                             className=\"control-group\"
    +                                             form-template-element=\"\"
    +                                             model=\"connection.params\"
    +                                             field=\"{name:'host',type:'string', mandatory:true, label:'主机'}\">
    +                                             <div
    +                                                 className=\"control-group\"
    +                                                 form-template-element=\"\"
    +                                                 model=\"connection.params\"
    +                                                 field=\"{name:'port', type:'int', mandatory:true, label:'端口'}\">
    +                                                 <div className=\"control-group\">
    +                                                     <label htmlFor=\"elasticConnParam_SSL\" className=\"control-label\">
    +                                                         使用 HTTPS
    +                                                     </label>
    +                                                     <div className=\"controls\">
    +                                                         <input
    +                                                             id=\"elasticConnParam_SSL\"
    +                                                             type=\"checkbox\"
    +                                                             value={connection.params.ssl}
    +                                                         />
    +                                                     </div>
    +                                                 </div>
    +                                                 <div className=\"control-group\">
    +                                                     <label htmlFor=\"elasticConnParam_SSL_any\" className=\"control-label\">
    +                                                         信任任何一个 SSL 认证
    +                                                     </label>
    +                                                     <div className=\"controls\">
    +                                                         <input
    +                                                             id=\"elasticConnParam_SSL_any\"
    +                                                             type=\"checkbox\"
    +                                                             disabled={!connection.params.ssl}
    +                                                             value={connection.params.trustAnySSLCertificate}
    +                                                         />
    +                                                         <span className=\"help-inline\">禁用 SSL 认证验证</span>
    +                                                     </div>
    +                                                 </div>
    +                                                 <div
    +                                                     className=\"control-group\"
    +                                                     form-template-element=\"\"
    +                                                     model=\"connection.params\"
    +                                                     field=\"{name:'username',type:'string', mandatory:false, label:'用户名'}\">
    +                                                     <div
    +                                                         className=\"control-group\"
    +                                                         form-template-element=\"\"
    +                                                         model=\"connection.params\"
    +                                                         field=\"{name:'password',type:'password', mandatory:false, label:'密码'}\">
    +                                                         <div
    +                                                             className=\"control-group\"
    +                                                             form-template-element=\"\"
    +                                                             model=\"connection.params\"
    +                                                             field=\"{name:'dialect', type:'advanced_select', mandatory:true, label:'方言',
    +                                 advancedChoices: [{value: 'ES_LE_2', label: '1.x or 2.x'}, {value: 'ES_5', label: '5.x or 6.x'}]}\">
    +                                                             <div include-no-scope=\"/templates/admin/fragments/connection-flags.html\" />
    +                                                         </div>
    +                                                     </div>
    +                                                 </div>
    + 
    +                                                 <div className=\"noflex small-lr-padding page-top-padding\">
    +                                                     <div include-no-scope=\"/templates/admin/fragments/connection-name-test-save.html\">
    +                                                         {testResult.connectionOK && !testResult.dialectChanged ? (
    +                                                             <div className=\"alert alert-success\">
    +                                                                 <strong>连接 OK</strong>, 找到 ES v.{' '}
    +                                                                 {testResult.version}
    +                                                             </div>
    +                                                         ) : null}
    +                                                         {testResult.dialectChanged && connectionDirty() ? (
    +                                                             <div className=\"alert alert-warning\">
    +                                                                 <strong>连接 OK</strong>, 但是 方言不匹配版本 (v.{' '}
    +                                                                 {testResult.version}) 同时被修改. 点击保存持久化此更新.
    +                                                             </div>
    +                                                         ) : null}
    +                                                         {testResult && !testResult.connectionOK ? (
    +                                                             <div className=\"alert alert-error\">
    +                                                                 <strong>连接错误</strong>:{' '}
    +                                                                 {testResult.connectionErrorMsg}
    +                                                             </div>
    +                                                         ) : null}
    +                                                     </div>
    +                                                 </div>
    +                                             </div>
    +                                         </div>
    +                                     </form>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;