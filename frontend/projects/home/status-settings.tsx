import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"h100 vertical-flex\" ng-init=\"uiState.settingsPane = $stateParams.selectedTab || 'metrics'\">
    +             <div block-api-error=\"\">
    +                 <div className=\"flex row-fluid\">
    +                     <div className=\"fh row-fluid\">
    +                         <div
    +                             className=\"span2 offset0 nav-list-sidebar sidebar-admin modal-footer-std-buttons\"
    +                             tab-model=\"uiState.settingsPane\">
    +                             <ul>
    +                                 <li sidebar-tab-l1-link=\"\" tab-name=\"metrics\" label=\"指标\" />
    +                                 <li sidebar-tab-l1-link=\"\" tab-name=\"checks\" label=\"检查\" />
    +                             </ul>
    +                         </div>
    + 
    +                         <div
    +                             className=\"span10 h100 offset0 boxed-next-to-sidebar no-padding\"
    +                             ng-switch=\"\"
    +                             on=\"uiState.settingsPane\">
    +                             <div ng-switch-when=\"metrics\" className=\"h100 oa\">
    +                                 <div className=\"h100 metrics-checks-selection\">
    +                                     <form
    +                                         className=\"dkuform-horizontal settings-block\"
    +                                         name=\"theform\"
    +                                         noValidate={true}
    +                                         ng-controller=\"ProjectMetricsEditionController\">
    +                                         <h4>插入一个指标值</h4>
    +                                         <div className=\"control-group\">
    +                                             <label className=\"control-label\">名称/值</label>
    +                                             <div className=\"controls\">
    +                                                 <input type=\"text\" value={newMetric.name} />
    +                                                 →
    +                                                 <input type=\"text\" value={newMetric.value} />
    +                                                 <button
    +                                                     className=\"btn btn-default\"
    +                                                     onClick={() => {
    +                                                         addMetricPoint(newMetric);
    +                                                     }}
    +                                                     disabled={!newMetric.name || !newMetric.value}>
    +                                                     添加
    +                                                 </button>
    +                                             </div>
    +                                         </div>
    +                                     </form>
    +                                 </div>
    +                             </div>
    +                             <div ng-switch-when=\"checks\" className=\"h100 oa\">
    +                                 <div className=\"h100 metrics-checks-selection\">
    +                                     <form
    +                                         className=\"dkuform-horizontal settings-block\"
    +                                         name=\"theform\"
    +                                         noValidate={true}
    +                                         ng-controller=\"ProjectChecksEditionController\">
    +                                         <h4>插入一个检查值</h4>
    +                                         <div className=\"control-group\">
    +                                             <label className=\"control-label\">名称/值</label>
    +                                             <div className=\"controls\">
    +                                                 <input type=\"text\" value={newCheck.name} />
    +                                                 :
    +                                                 <select dku-bs-select=\"\" value={newCheck.value}>
    +                                                     <option value=\"OK\">好的</option>
    +                                                     <option value=\"ERROR\">错误</option>
    +                                                     <option value=\"WARNING\">警告</option>
    +                                                     <option value=\"EMPTY\">空</option>
    +                                                 </select>
    +                                                 <button
    +                                                     className=\"btn btn-default\"
    +                                                     onClick={() => {
    +                                                         addCheckPoint(newCheck);
    +                                                     }}
    +                                                     disabled={!newCheck.name || !newCheck.value}>
    +                                                     添加
    +                                                 </button>
    +                                             </div>
    +                                         </div>
    +                                         <div className=\"control-group\">
    +                                             <label className=\"control-label\">消息</label>
    +                                             <div className=\"controls\">
    +                                                 <input type=\"text\" value={newCheck.message} />
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