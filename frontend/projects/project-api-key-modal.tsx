import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 dku-modal\" ng-controller=\"EditProjectAPIKeyModalController\">
    +             <div
    +                 dku-modal-header-with-totem=\"\"
    +                 modal-title={creation ? '新建 API 键' : '编辑 API 键'}
    +                 modal-totem=\"icon-unlock-alt\">
    +                 <form className=\"dkuform-modal-horizontal dkuform-modal-wrapper\" name=\"theform\">
    +                     <div className=\"modal-body\" style=\"max-height: 350px !important;\">
    +                         <div block-api-error=\"\">
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">标签</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={apiKey.label} placeholder=\"Label\" />
    +                                 </div>
    +                             </div>
    + 
    +                             <fieldset
    +                                 className=\"control-group\"
    +                                 disabled={!appConfig.admin || !appConfig.impersonationEnabled}>
    +                                 <label className=\"control-label\">模仿 DSS 用户</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={apiKey.dssUserForImpersonation} />
    +                                     <div className=\"help-inline\">
    +                                         这仅用于选择用户 模仿任务。 它不授予此DSS权限到此用户
    +                                         {appConfig.impersonationEnabled && !appConfig.admin ? (
    +                                             <div className=\"alert alert-error\">
    +                                                 您可能无法编辑此内容，因为您不是DSS管理员
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                 </div>
    +                             </fieldset>
    + 
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">描述</label>
    +                                 <div className=\"controls\">
    +                                     <input
    +                                         type=\"text\"
    +                                         value={apiKey.description}
    +                                         placeholder=\"What should this key be used for?\"
    +                                     />
    +                                 </div>
    +                             </div>
    + 
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">工程范围的特权</label>
    +                                 <textarea
    +                                     json-object-pretty-view=\"\"
    +                                     value={apiKey.projectPrivileges}
    +                                     style=\"width: 100%; box-sizing: border-box; min-height: 150px\">
    +                                     {' '}
    +                                     &lt;/div&gt; &lt;div class=\"control-group\"&gt; &lt;label class=\"control-label\"&gt;
    +                                     执行 SQL &lt;/label&gt; &lt;div class=\"controls\"&gt; &lt;label&gt; &lt;input
    +                                     type=\"checkbox\" ng-model=\"apiKey.execSQLLike\" id=\"execSQLLikeInput\" /&gt; &lt;span
    +                                     class=\"help-inline\"&gt;这些用户是否可以在DSS中定义的连接上运行SQL查询.&lt;/span&gt;
    +                                     &lt;/label&gt; &lt;/div&gt; &lt;/div&gt; &lt;div class=\"control-group\"&gt; &lt;label
    +                                     class=\"control-label\"&gt; 数据集特权 &lt;/label&gt; &lt;textarea
    +                                     json-object-pretty-view ng-model=\"apiKey.localDatasets\" style=\"width: 100%;
    +                                     box-sizing: border-box; min-height: 150px\"/&gt; &lt;/div&gt; &lt;div
    +                                     class=\"control-group\"&gt; &lt;label class=\"control-label\"&gt; &lt;a class=\"btn
    +                                     btn-default\" ng-click=\"makeAdmin(apiKey)\"&gt; 管理 &lt;/a&gt; &lt;/label&gt;
    +                                     &lt;/div&gt; &lt;/div&gt; &lt;div class=\"modal-footer modal-footer-std-buttons\"&gt;
    +                                     &lt;button type=\"button\" class=\"btn btn-default\"
    +                                     ng-click=\"dismiss()\"&gt;取消&lt;/button&gt; &lt;button type=\"submit\" class=\"btn
    +                                     btn-primary\" ng-click=\"create()\" ng-if=\"creation\"&gt;创建&lt;/button&gt; &lt;button
    +                                     type=\"submit\" class=\"btn btn-primary\" ng-click=\"save()\"
    +                                     ng-if=\"!creation\"&gt;保存&lt;/button&gt; &lt;/div&gt; &lt;/form&gt; &lt;/div&gt;
    +                                 </textarea>
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;