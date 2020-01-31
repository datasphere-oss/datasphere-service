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
    +             ng-controller=\"CreateAndPinInsightModalController\"
    +             auto-size=\"false\">
    +             <div
    +                 dku-modal-header-with-totem=\"\"
    +                 modal-title=\"Create insight and add to dashboard\"
    +                 modal-totem=\"icon-dku-publish\">
    +                 <form name=\"createAndPinInsightForm\" className=\"dkuform-modal-wrapper\">
    +                     {insight && insight.type ? (
    +                         <div className=\"modal-body\">
    +                             <div block-api-error=\"\" className=\"modal-error\" />
    + 
    +                             {initiated && pinningOrders.length <= 0 ? (
    +                                 <div className=\"alert alert-warning\">
    +                                     {dashboards.length <= 0 && allDashboardsCount > 0 ? (
    +                                         <span>此项目没有您可以写入的仪表板.</span>
    +                                     ) : null}
    +                                     {dashboards.length <= 0 && allDashboardsCount == 0 ? (
    +                                         <span>该项目没有仪表板.</span>
    +                                     ) : null}
    +                                     无需添加到任何仪表板即可创建Insight.
    +                                 </div>
    +                             ) : null}
    + 
    +                             {missingReaderAuthorizations && missingReaderAuthorizations.length > 0 ? (
    +                                 <div className=\"alert alert-warning\">
    +                                     <div>This source is not yet shared with dashboard-only users.</div>
    +                                     {projectSummary.canManageDashboardAuthorizations ? (
    +                                         <label style=\"margin-top: 10px\">
    +                                             <input
    +                                                 type=\"checkbox\"
    +                                                 value={addReaderAuthorization}
    +                                                 style=\"margin: -1px 5px 0 0\"
    +                                             />
    +                                             添加源到授权的对象
    +                                         </label>
    +                                     ) : null}
    +                                     {!addReaderAuthorization ? (
    +                                         <div style=\"padding-top: 5px;\">
    +                                             <i className=\"icon-warning-sign\" />&nbsp;<strong>
    +                                                 仅限仪表板的用户将无法看到此洞察.
    +                                             </strong>
    +                                         </div>
    +                                     ) : null}
    +                                 </div>
    +                             ) : null}
    + 
    +                             {dashboards.length > 0 ? (
    +                                 <div include-no-scope=\"/templates/dashboards/insights/multi-pin-insight-table.html\" />
    +                             ) : null}
    +                         </div>
    +                     ) : null}
    + 
    +                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 dismiss();
    +                             }}>
    +                             取消
    +                         </button>
    +                         <button
    +                             type=\"submit\"
    +                             className=\"btn btn-primary\"
    +                             id=\"qa_webapp_publish-create-button\"
    +                             onClick={() => {
    +                                 sendCreateAndPinOrders();
    +                             }}>
    +                             创建
    +                         </button>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;