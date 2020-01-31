import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div
    +             className=\"modal modal3 multipin-insight-modal multichart-modal dku-modal modal-wide modal-half-panes\"
    +             ng-controller=\"CreateAndPinInsightsModalController\"
    +             auto-size=\"false\">
    +             <div dku-modal-header-with-totem=\"\" modal-title=\"创建透视, 添加到仪表盘\" modal-totem=\"icon-dku-publish\">
    +                 <form name=\"createAndPinInsightForm\" className=\"dkuform-modal-wrapper\">
    +                     {insightData && insightData.items && insightData.items.length ? (
    +                         <div className=\"modal-body horizontal-flex\">
    +                             <div className=\"half-pane quarter-pane\">
    +                                 {insightData.items ? (
    +                                     <ul className=\"noflex items-list\">
    +                                         {insightData.items.map((item, index: number) => {
    +                                             return (
    +                                                 <li
    +                                                     key={`item-${index}`}
    +                                                     id={`items-list__item-${$index}`}
    +                                                     className=\"items-list__item\"
    +                                                     scroll-to-me={insightData.items[$index].selected}
    +                                                     scroll-align=\"center\">
    +                                                     {insightData.type === 'chart' ? (
    +                                                         <div className=\"items-list__item-wrap\">
    +                                                             <input
    +                                                                 id={`items-list_input-${$index}`}
    +                                                                 className=\"items-list__input\"
    +                                                                 type=\"checkbox\"
    +                                                                 value={insightData.items[$index].selected}
    +                                                             />
    +                                                             <label
    +                                                                 htmlFor={`items-list_input-${$index}`}
    +                                                                 className=\"items-list__label\">
    +                                                                 {!item.def.thumbnailData ? (
    +                                                                     <span
    +                                                                         className=\"no-thumb\"
    +                                                                         ng-inject=\"ChartIconUtils\">
    +                                                                         <img
    +                                                                             className=\"items-list__thumbnail\"
    +                                                                             src={ChartIconUtils.computeChartIcon(
    +                                                                                 item.def.type,
    +                                                                                 item.def.variant,
    +                                                                                 !!analysisId
    +                                                                             )}
    +                                                                         />
    +                                                                     </span>
    +                                                                 ) : null}
    +                                                                 {item.def.thumbnailData ? (
    +                                                                     <img
    +                                                                         className=\"items-list__thumbnail\"
    +                                                                         src={item.def.thumbnailData}
    +                                                                         dku-better-tooltip=\"\"
    +                                                                         dbt-placement=\"top\"
    +                                                                         dbt-title={item.def.name}
    +                                                                     />
    +                                                                 ) : null}
    +                                                                 {item.def.name ? (
    +                                                                     <span className=\"items-list__name\">
    +                                                                         {item.def.name}
    +                                                                     </span>
    +                                                                 ) : null}
    +                                                             </label>
    +                                                         </div>
    +                                                     ) : null}
    +                                                 </li>
    +                                             );
    +                                         })}
    +                                     </ul>
    +                                 ) : null}
    +                             </div>
    +                             <div className=\"half-pane threequarters-pane\">
    +                                 <div block-api-error=\"\" className=\"modal-error\" />
    + 
    +                                 {initiated && pinningOrders.length <= 0 ? (
    +                                     <div className=\"alert alert-warning\">
    +                                         {dashboards.length <= 0 && allDashboardsCount > 0 ? (
    +                                             <span>此项目没有您可以写入的仪表板.</span>
    +                                         ) : null}
    +                                         {dashboards.length <= 0 && allDashboardsCount == 0 ? (
    +                                             <span>该项目没有仪表板.</span>
    +                                         ) : null}
    +                                         无需添加到任何仪表板即可创建Insight
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {missingReaderAuthorizations && missingReaderAuthorizations.length > 0 ? (
    +                                     <div className=\"alert alert-warning\">
    +                                         <div>此信息源尚未与仅限信息中心的用户共享.</div>
    +                                         {projectSummary.canManageDashboardAuthorizations ? (
    +                                             <label style=\"margin-top: 10px\">
    +                                                 <input
    +                                                     type=\"checkbox\"
    +                                                     value={addReaderAuthorization}
    +                                                     style=\"margin: -1px 5px 0 0\"
    +                                                 />
    +                                                 将此源添加到授权对象
    +                                             </label>
    +                                         ) : null}
    +                                         {!addReaderAuthorization ? (
    +                                             <div style=\"padding-top: 5px;\">
    +                                                 <i className=\"icon-warning-sign\" />&nbsp;<strong>
    +                                                     仅限仪表板的用户将无法看到此洞察力.
    +                                                 </strong>
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                 ) : null}
    + 
    +                                 {dashboards.length > 0 ? (
    +                                     <div include-no-scope=\"/templates/dashboards/insights/multi-pin-insight-table.html\" />
    +                                 ) : null}
    +                             </div>
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
    +                             disabled={!canCreate()}
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