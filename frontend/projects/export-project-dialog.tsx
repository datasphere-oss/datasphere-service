import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <div dku-modal-header=\"\" modal-title=\"工程导出\" modal-class=\"has-border\">
    +                 <form className=\"dkuform-modal-wrapper\">
    +                     <div className=\"modal-body\">
    +                         <h2 className=\"settings-section-title mtop0\">数据集数据</h2>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportUploads} />导出所有 'uploaded' 数据集
    +                         </label>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportAllInputDatasets} />导出所有 'input'
    +                             数据集
    +                         </label>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportManagedFS} />导出所有托管文件数据集
    +                         </label>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportInsightsData} />导出静态洞察数据
    +                         </label>
    + 
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportAllDatasets} />导出所有数据集
    +                         </label>
    +                         <div className=\"smallgrey\">NB: 分区数据集不能导出.</div>
    + 
    +                         <h2 className=\"settings-section-title\">托管文件夹数据</h2>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportAllInputManagedFolders} />导出所有 '输入'
    +                             托管文件夹数据
    +                         </label>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportManagedFolders} />导出所有托管文件夹数据
    +                         </label>
    + 
    +                         <h2 className=\"settings-section-title\">机器学习数据</h2>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportAnalysisModels} />导出所有分析模型数据
    +                         </label>
    +                         <label className=\"checkbox\">
    +                             <input type=\"checkbox\" value={exportOptions.exportSavedModels} />导出所有保存模型的数据
    +                         </label>
    + 
    +                         {!uiState.showAdvancedOptions ? (
    +                             <a
    +                                 onClick={() => {
    +                                     uiState.showAdvancedOptions = true;
    +                                 }}>
    +                                 高级选项
    +                             </a>
    +                         ) : null}
    + 
    +                         {uiState.showAdvancedOptions && appConfig.gitMode == 'PROJECT' ? (
    +                             <div>
    +                                 <h2 className=\"settings-section-title\">其他</h2>
    +                                 <label className=\"checkbox\">
    +                                     <input type=\"checkbox\" value={exportOptions.exportGitRepository} />导出Git仓库
    +                                 </label>
    +                             </div>
    +                         ) : null}
    +                         {uiState.showAdvancedOptions ? (
    +                             <div>
    +                                 <h2 className=\"settings-section-title\">必要补丁</h2>
    +                                 <label className=\"checkbox\">
    +                                     <input type=\"checkbox\" value={exportOptions.useManualPluginsInfo} />自定义
    +                                 </label>
    +                                 {exportOptions.useManualPluginsInfo ? (
    +                                     <div className=\"common-styles-only\" style=\"min-height: 60px;\">
    +                                         <textarea
    +                                             style=\"width: 100%; min-height: 60px\"
    +                                             json-array-pretty-view=\"\"
    +                                             value={exportOptions.manualPluginsInfo}>
    +                                             {' '}
    +                                             &lt;/div&gt; &lt;/div&gt; &lt;/div&gt; &lt;div class=\"modal-footer
    +                                             modal-footer-std-buttons\" &gt; &lt;button type=\"button\" class=\"btn
    +                                             btn-default\" ng-click=\"dismiss()\"&gt;Cancel&lt;/button&gt; &lt;button
    +                                             type=\"submit\" class=\"btn btn-primary\"
    +                                             ng-click=\"export()\"&gt;Export&lt;/button&gt; &lt;/div&gt; &lt;/form&gt;
    +                                             &lt;/div&gt;
    +                                         </textarea>
    +                                     </div>
    +                                 ) : null}
    +                             </div>
    +                         ) : null}
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;