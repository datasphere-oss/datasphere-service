import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"modal modal3 multipin-insight-modal dku-modal\"
    +             ng-controller=\"MultiPinInsightModalController\"
    +             auto-size=\"false\">
    +             <div
    +                 className=\"noflex has-border\"
    +                 dku-modal-header=\"\"
    +                 modal-title=\"Add to dashboards\"
    +                 modal-totem=\"icon-dku-publish\">
    +                 <form name=\"multiPinInsightForm\" className=\"dkuform-modal-wrapper\">
    +                     {insight.type ? (
    +                         <div className=\"modal-body\">
    +                             <div block-api-error=\"\">
    +                                 {initiated && pinningOrders.length <= 0 ? (
    +                                     <div className=\"alert alert-error\">
    +                                         {dashboards.length > 0 ? <span>请选择至少一个仪表盘.</span> : null}
    +                                         {!dashboards.length ? <span>你没有任何仪表盘, 请创建一个.</span> : null}
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 <div include-no-scope=\"/templates/dashboards/insights/multi-pin-insight-table.html\">
    +                                     {advancedMode ? (
    +                                         <div className=\"control-group pointer-mode\">
    +                                             <div className=\"controls\">
    +                                                 <label>
    +                                                     <input type=\"checkbox\" value={pointerMode.mode} />
    +                                                     Create tiles pointing to the selected insight instead of duplicating
    +                                                     it.
    +                                                 </label>
    +                                             </div>
    +                                         </div>
    +                                     ) : null}
    +                                 </div>
    + 
    +                                 <div className=\"modal-footer modal-footer-std-buttons\">
    +                                     {dashboards.length > 0 ? (
    +                                         <div className=\"advanced-parameters\">
    +                                             <label>
    +                                                 <input type=\"checkbox\" value={advancedMode} />&nbsp;Advanced parameters
    +                                             </label>
    +                                         </div>
    +                                     ) : null}
    + 
    +                                     <button
    +                                         type=\"button\"
    +                                         className=\"btn btn-default\"
    +                                         onClick={() => {
    +                                             dismiss();
    +                                         }}>
    +                                         取消
    +                                     </button>
    +                                     <button
    +                                         type=\"submit\"
    +                                         className=\"btn btn-primary\"
    +                                         id=\"qa_insight_publish-pin-button\"
    +                                         onClick={() => {
    +                                             sendPinningOrders();
    +                                         }}
    +                                         disabled={pinningOrders.length <= 0}>
    +                                         Pin
    +                                     </button>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     ) : null}
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;