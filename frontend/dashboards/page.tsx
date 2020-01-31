import * as React from 'react';
     
     export interface TestComponentProps {
         [key: string]: any;
     }
     
     const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
         return (
             <div
                 className=\"{editable: editable, 'has-grid': editable &amp;&amp; showGrid}\"
                 onMouseUp={(event: React.SyntheticEvent<HTMLElement>) => {
                     dashboardClicked(event);
                 }}
                 style=\"page.backgroundColor &amp;&amp; {'background-color':page.backgroundColor}\">
                 <div block-api-error=\"\" />
     
                 {page.showTitle ? (
                     <h1 className=\"{'default-background' : !page.backgroundColor}\">
                         {page.title || 'Slide '  pageIdx  1}
                     </h1>
                 ) : null}
     
                 <div style=\"position: relative; flex: 1 0 auto;\">
                     {!page.grid.tiles.length ? (
                         <div className=\"fh h100\" style=\"z-index: 5;\">
                             {editable ? (
                                 <div className=\"centered-info\">
                                     开始构建你的 {pageIdx == 0 ? 'dashboard' : 'slide'}
                                     <div style=\"width: 300px; margin: 30px auto; text-align: left;\">
                                         <p className=\"small\" style=\"color:#757575;\">
                                             点击{' '}
                                             <i className=\"icon-plus-sign inline-left-panel-plus\"> 按钮添加瓦片到胶片</i>
                                         </p>
                                         <i className=\"icon-plus-sign inline-left-panel-plus\">
                                             <p className=\"small\" style=\"color:#757575;\">
                                                 点击 <i className=\"icon-plus-sign inline-page-plus\">按钮添加到胶片</i>
                                             </p>
                                             <i className=\"icon-plus-sign inline-page-plus\" />
                                         </i>
                                     </div>
                                     <i className=\"icon-plus-sign inline-left-panel-plus\">
                                         <i className=\"icon-plus-sign inline-page-plus\" />
                                     </i>
                                 </div>
                             ) : null}
                             <i className=\"icon-plus-sign inline-left-panel-plus\">
                                 <i className=\"icon-plus-sign inline-page-plus\">
                                     {!editable ? (
                                         <div className=\"centered-info\">
                                             此胶片为空
                                             {canEditDashboard ? (
                                                 <small style=\"font-size: 18px;\">
                                                     <br />
                                                     <br />转换到{' '}
                                                     <NavLink to=\"projects.project.dashboards.dashboard.edit\">
                                                         编辑模式
                                                     </NavLink>{' '}
                                                     到添加瓦片
                                                 </small>
                                             ) : null}
                                         </div>
                                     ) : null}
                                 </i>
                             </i>
                         </div>
                     ) : null}
                     <i className=\"icon-plus-sign inline-left-panel-plus\">
                         <i className=\"icon-plus-sign inline-page-plus\">
                             {editable && showGrid ? (
                                 <div
                                     grid-display=\"\"
                                     background-color=\"page.backgroundColor\"
                                     grid-format=\"dashboard.gridFormat\">
                                     {!gridListRendered ? (
                                         <div className=\"loading\">
                                             <spinner speed=\"2\" />
                                         </div>
                                     ) : null}
     
                                     <ul className=\"dashboard-grid dashboard-export-grid\">
                                         {page.grid.tiles.map((tile, index: number) => {
                                             return (
                                                 <li
                                                     key={`item-${index}`}
                                                     className=\"{'selected': editable &amp;&amp; tile==selectedTile, 'invisible' : !tile.$added, 'transparent' : tile.tileParams.isTransparent}\"
                                                     scroll-to-me={editable && tile.$added && tile == selectedTile}
                                                     data-id={tile.$tileId}
                                                     data-x={tile.box.left >= 0 ? tile.box.left : null}
                                                     data-y={tile.box.top >= 0 ? tile.box.top : null}
                                                     data-w={tile.box.width}
                                                     data-h={tile.box.height}
                                                     ng-init=\"addTileToGridList(tile)\"
                                                     onMouseUp={(event: React.SyntheticEvent<HTMLElement>) => {
                                                         selectTile(tile, event);
                                                     }}
                                                     onMouseLeave={() => {
                                                         tile.$showOverlay = false;
                                                     }}>
                                                     <div
                                                         className=\"{'deleted': accessMap[tile.insightId] == 'DELETED', 'error-loading': isErrorMap[tile.$tileId]}\"
                                                         full-click=\"dashboard\"
                                                         ng-right-click=\"editable &amp;&amp; openTileMenu($event, tile)\"
                                                         style=\"!isErrorMap[tile.$tileId] &amp;&amp;  tile.borderColor &amp;&amp; {'border-color' : tile.borderColor, 'border-width' : '2px'}\">
                                                         {!editable &&
                                                         (tile.clickAction != 'DO_NOTHING' &&
                                                             DashboardUtils.tileCanLink(tile)) ? (
                                                             <a
                                                                 className=\"tile-link\"
                                                                 href={getTargetHref(tile)}
                                                                 main-click=\"dashboard\"
                                                             />
                                                         ) : null}
     
                                                         <div className=\"h100 vertical-flex\" style=\"position: relative;\">
                                                             {tile.showTitle != 'NO' || editable ? (
                                                                 <div className=\"{'display-on-hover' : tile.showTitle == 'MOUSEOVER' &amp;&amp; !editable}\">
                                                                     {/* Warning sign if insight is not accessible to readers */}
                                                                     {editable &&
                                                                     insightsMap[tile.insightId] &&
                                                                     accessMap[tile.insightId] == 'ANALYST' ? (
                                                                         <div className=\"noflex\">
                                                                             <i
                                                                                 className=\"icon-warning-sign\"
                                                                                 style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                                                                 onClick={() => {
                                                                                     openInsightAccessModal(
                                                                                         insightsMap[tile.insightId]
                                                                                     );
                                                                                 }}
                                                                                 dashboard-no-select=\"\"
                                                                             />
                                                                         </div>
                                                                     ) : null}
                                                                     <i
                                                                         className=\"icon-warning-sign\"
                                                                         style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                                                         onClick={() => {
                                                                             openInsightAccessModal(
                                                                                 insightsMap[tile.insightId]
                                                                             );
                                                                         }}
                                                                         dashboard-no-select=\"\">
                                                                         {editable &&
                                                                         tile.displayMode == 'IMAGE' &&
                                                                         (accessMap[tile.insightId] == 'DELETED' ||
                                                                             accessMap[tile.insightId] == 'NOT_EXPOSED') ? (
                                                                             <div className=\"noflex error-text-color\">
                                                                                 <i
                                                                                     className=\"icon-warning-sign\"
                                                                                     style=\"margin-right: 7px; cursor: pointer;\">
                                                                                     {' '}
                                                                                     INSIGHT {accessMap[tile.insightId]}
                                                                                 </i>
                                                                             </div>
                                                                         ) : null}
                                                                         <i
                                                                             className=\"icon-warning-sign\"
                                                                             style=\"margin-right: 7px; cursor: pointer;\">
                                                                             {tile.showTitle ? (
                                                                                 <div
                                                                                     className=\"tile-title oh\"
                                                                                     style=\"flex: 0 1 auto;\">
                                                                                     <div className=\"mx-textellipsis\">
                                                                                         {tile.title ||
                                                                                             insightsMap[tile.insightId]
                                                                                                 .name}
                                                                                     </div>
                                                                                 </div>
                                                                             ) : null}
                                                                             <div className=\"tile-target flex\">
                                                                                 {tile.tileType == 'INSIGHT' ? (
                                                                                     <a
                                                                                         dashboard-no-select=\"\"
                                                                                         href={getTargetHref(tile)}>
                                                                                         <i className=\"icon-external-link\" />
                                                                                     </a>
                                                                                 ) : null}
                                                                                 <i className=\"icon-external-link\" />
                                                                             </div>
                                                                             <i className=\"icon-external-link\">
                                                                                 {editable ? (
                                                                                     <div className=\"tile-actions noflex\">
                                                                                         <a
                                                                                             onClick={() => {
                                                                                                 openMoveCopyTileModal(tile);
                                                                                             }}>
                                                                                             <i className=\"icon-copy\" />
                                                                                         </a>
                                                                                         <i className=\"icon-copy\">
                                                                                             <a
                                                                                                 onClick={() => {
                                                                                                     deleteTile(tile);
                                                                                                 }}>
                                                                                                 <i
                                                                                                     className=\"icon-trash\"
                                                                                                     dashboard-no-select=\"\"
                                                                                                 />
                                                                                             </a>
                                                                                             <i
                                                                                                 className=\"icon-trash\"
                                                                                                 dashboard-no-select=\"\"
                                                                                             />
                                                                                         </i>
                                                                                     </div>
                                                                                 ) : null}
                                                                                 <i className=\"icon-copy\">
                                                                                     <i
                                                                                         className=\"icon-trash\"
                                                                                         dashboard-no-select=\"\"
                                                                                     />
                                                                                 </i>
                                                                             </i>
                                                                         </i>
                                                                     </i>
                                                                 </div>
                                                             ) : null}
                                                             <i
                                                                 className=\"icon-warning-sign\"
                                                                 style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                                                 onClick={() => {
                                                                     openInsightAccessModal(insightsMap[tile.insightId]);
                                                                 }}
                                                                 dashboard-no-select=\"\">
                                                                 <i
                                                                     className=\"icon-warning-sign\"
                                                                     style=\"margin-right: 7px; cursor: pointer;\">
                                                                     <i className=\"icon-external-link\">
                                                                         <i className=\"icon-copy\">
                                                                             <i
                                                                                 className=\"icon-trash\"
                                                                                 dashboard-no-select=\"\">
                                                                                 <div className=\"flex\">
                                                                                     <div className=\"fh h100\">
                                                                                         {tile.tileType != 'INSIGHT' ||
                                                                                         tile.displayMode == 'IMAGE' ||
                                                                                         insightsMap[tile.insightId] ? (
                                                                                             <div>
                                                                                                 <DashboardTile
                                                                                                     tile=\"tile\"
                                                                                                     editable=\"editable\"
                                                                                                     insight=\"insightsMap[tile.insightId]\"
                                                                                                     hook=\"hook\"
                                                                                                     className=\"fh\"
                                                                                                     style=\"overflow: hidden;\"
                                                                                                 />
                                                                                             </div>
                                                                                         ) : null}
     
                                                                                         {tile.displayMode == 'INSIGHT' &&
                                                                                         tile.displayMode != 'IMAGE' &&
                                                                                         !insightsMap[tile.insightId] ? (
                                                                                             <div
                                                                                                 ng-switch=\"\"
                                                                                                 on=\"accessMap[tile.insightId]\"
                                                                                                 className=\"h100\">
                                                                                                 <div
                                                                                                     ng-switch-when=\"DELETED\"
                                                                                                     className=\"centered-info\">
                                                                                                     洞察删除
                                                                                                 </div>
     
                                                                                                 <div
                                                                                                     ng-switch-when=\"ANALYST\"
                                                                                                     className=\"vertical-flex fh center-children\">
                                                                                                     <i
                                                                                                         className=\"icon-warning-sign text-error\"
                                                                                                         toggle=\"tooltip\"
                                                                                                         title=\"Insight not accessible to dashboard users\"
                                                                                                         placement=\"bottom\"
                                                                                                         container=\"body\"
                                                                                                         style=\"font-size: 40px\"
                                                                                                     />
                                                                                                 </div>
                                                                                                 <i
                                                                                                     className=\"icon-warning-sign text-error\"
                                                                                                     toggle=\"tooltip\"
                                                                                                     title=\"Insight not accessible to dashboard users\"
                                                                                                     placement=\"bottom\"
                                                                                                     container=\"body\"
                                                                                                     style=\"font-size: 40px\">
                                                                                                     <div
                                                                                                         ng-switch-when=\"NOT_EXPOSED\"
                                                                                                         className=\"vertical-flex fh center-children\">
                                                                                                         <i
                                                                                                             className=\"icon-warning-sign text-error\"
                                                                                                             toggle=\"tooltip\"
                                                                                                             title=\"Foreign source not exposed to this project\"
                                                                                                             placement=\"bottom\"
                                                                                                             container=\"body\"
                                                                                                             style=\"font-size: 40px\"
                                                                                                         />
                                                                                                     </div>
                                                                                                     <i
                                                                                                         className=\"icon-warning-sign text-error\"
                                                                                                         toggle=\"tooltip\"
                                                                                                         title=\"Foreign source not exposed to this project\"
                                                                                                         placement=\"bottom\"
                                                                                                         container=\"body\"
                                                                                                         style=\"font-size: 40px\"
                                                                                                     />
                                                                                                 </i>
                                                                                             </div>
                                                                                         ) : null}
                                                                                         <i
                                                                                             className=\"icon-warning-sign text-error\"
                                                                                             toggle=\"tooltip\"
                                                                                             title=\"Insight not accessible to dashboard users\"
                                                                                             placement=\"bottom\"
                                                                                             container=\"body\"
                                                                                             style=\"font-size: 40px\">
                                                                                             <i
                                                                                                 className=\"icon-warning-sign text-error\"
                                                                                                 toggle=\"tooltip\"
                                                                                                 title=\"Foreign source not exposed to this project\"
                                                                                                 placement=\"bottom\"
                                                                                                 container=\"body\"
                                                                                                 style=\"font-size: 40px\"
                                                                                             />
                                                                                         </i>
                                                                                     </div>
                                                                                     <i
                                                                                         className=\"icon-warning-sign text-error\"
                                                                                         toggle=\"tooltip\"
                                                                                         title=\"Insight not accessible to dashboard users\"
                                                                                         placement=\"bottom\"
                                                                                         container=\"body\"
                                                                                         style=\"font-size: 40px\">
                                                                                         <i
                                                                                             className=\"icon-warning-sign text-error\"
                                                                                             toggle=\"tooltip\"
                                                                                             title=\"Foreign source not exposed to this project\"
                                                                                             placement=\"bottom\"
                                                                                             container=\"body\"
                                                                                             style=\"font-size: 40px\"
                                                                                         />
                                                                                     </i>
                                                                                 </div>
                                                                                 <i
                                                                                     className=\"icon-warning-sign text-error\"
                                                                                     toggle=\"tooltip\"
                                                                                     title=\"Insight not accessible to dashboard users\"
                                                                                     placement=\"bottom\"
                                                                                     container=\"body\"
                                                                                     style=\"font-size: 40px\">
                                                                                     <i
                                                                                         className=\"icon-warning-sign text-error\"
                                                                                         toggle=\"tooltip\"
                                                                                         title=\"Foreign source not exposed to this project\"
                                                                                         placement=\"bottom\"
                                                                                         container=\"body\"
                                                                                         style=\"font-size: 40px\"
                                                                                     />
                                                                                 </i>
                                                                             </i>
                                                                         </i>
                                                                     </i>
                                                                 </i>
                                                             </i>
                                                         </div>
                                                         <i
                                                             className=\"icon-warning-sign\"
                                                             style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                                             onClick={() => {
                                                                 openInsightAccessModal(insightsMap[tile.insightId]);
                                                             }}
                                                             dashboard-no-select=\"\">
                                                             <i
                                                                 className=\"icon-warning-sign\"
                                                                 style=\"margin-right: 7px; cursor: pointer;\">
                                                                 <i className=\"icon-external-link\">
                                                                     <i className=\"icon-copy\">
                                                                         <i className=\"icon-trash\" dashboard-no-select=\"\">
                                                                             <i
                                                                                 className=\"icon-warning-sign text-error\"
                                                                                 toggle=\"tooltip\"
                                                                                 title=\"Insight not accessible to dashboard users\"
                                                                                 placement=\"bottom\"
                                                                                 container=\"body\"
                                                                                 style=\"font-size: 40px\">
                                                                                 <i
                                                                                     className=\"icon-warning-sign text-error\"
                                                                                     toggle=\"tooltip\"
                                                                                     title=\"Foreign source not exposed to this project\"
                                                                                     placement=\"bottom\"
                                                                                     container=\"body\"
                                                                                     style=\"font-size: 40px\"
                                                                                 />
                                                                             </i>
                                                                         </i>
                                                                     </i>
                                                                 </i>
                                                             </i>
                                                         </i>
                                                     </div>
                                                     <i
                                                         className=\"icon-warning-sign\"
                                                         style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                                         onClick={() => {
                                                             openInsightAccessModal(insightsMap[tile.insightId]);
                                                         }}
                                                         dashboard-no-select=\"\">
                                                         <i
                                                             className=\"icon-warning-sign\"
                                                             style=\"margin-right: 7px; cursor: pointer;\">
                                                             <i className=\"icon-external-link\">
                                                                 <i className=\"icon-copy\">
                                                                     <i className=\"icon-trash\" dashboard-no-select=\"\">
                                                                         <i
                                                                             className=\"icon-warning-sign text-error\"
                                                                             toggle=\"tooltip\"
                                                                             title=\"Insight not accessible to dashboard users\"
                                                                             placement=\"bottom\"
                                                                             container=\"body\"
                                                                             style=\"font-size: 40px\">
                                                                             <i
                                                                                 className=\"icon-warning-sign text-error\"
                                                                                 toggle=\"tooltip\"
                                                                                 title=\"Foreign source not exposed to this project\"
                                                                                 placement=\"bottom\"
                                                                                 container=\"body\"
                                                                                 style=\"font-size: 40px\"
                                                                             />
                                                                         </i>
                                                                     </i>
                                                                 </i>
                                                             </i>
                                                         </i>
                                                     </i>
                                                 </li>
                                             );
                                         })}
                                         <i
                                             className=\"icon-warning-sign\"
                                             style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                             onClick={() => {
                                                 openInsightAccessModal(insightsMap[tile.insightId]);
                                             }}
                                             dashboard-no-select=\"\">
                                             <i className=\"icon-warning-sign\" style=\"margin-right: 7px; cursor: pointer;\">
                                                 <i className=\"icon-external-link\">
                                                     <i className=\"icon-copy\">
                                                         <i className=\"icon-trash\" dashboard-no-select=\"\">
                                                             <i
                                                                 className=\"icon-warning-sign text-error\"
                                                                 toggle=\"tooltip\"
                                                                 title=\"Insight not accessible to dashboard users\"
                                                                 placement=\"bottom\"
                                                                 container=\"body\"
                                                                 style=\"font-size: 40px\">
                                                                 <i
                                                                     className=\"icon-warning-sign text-error\"
                                                                     toggle=\"tooltip\"
                                                                     title=\"Foreign source not exposed to this project\"
                                                                     placement=\"bottom\"
                                                                     container=\"body\"
                                                                     style=\"font-size: 40px\">
                                                                     <li className=\"position-highlight\">
                                                                         <div className=\"inner\" />
                                                                     </li>
                                                                 </i>
                                                             </i>
                                                         </i>
                                                     </i>
                                                 </i>
                                             </i>
                                         </i>
                                     </ul>
                                     <i
                                         className=\"icon-warning-sign\"
                                         style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                         onClick={() => {
                                             openInsightAccessModal(insightsMap[tile.insightId]);
                                         }}
                                         dashboard-no-select=\"\">
                                         <i className=\"icon-warning-sign\" style=\"margin-right: 7px; cursor: pointer;\">
                                             <i className=\"icon-external-link\">
                                                 <i className=\"icon-copy\">
                                                     <i className=\"icon-trash\" dashboard-no-select=\"\">
                                                         <i
                                                             className=\"icon-warning-sign text-error\"
                                                             toggle=\"tooltip\"
                                                             title=\"Insight not accessible to dashboard users\"
                                                             placement=\"bottom\"
                                                             container=\"body\"
                                                             style=\"font-size: 40px\">
                                                             <i
                                                                 className=\"icon-warning-sign text-error\"
                                                                 toggle=\"tooltip\"
                                                                 title=\"Foreign source not exposed to this project\"
                                                                 placement=\"bottom\"
                                                                 container=\"body\"
                                                                 style=\"font-size: 40px\"
                                                             />
                                                         </i>
                                                     </i>
                                                 </i>
                                             </i>
                                         </i>
                                     </i>
                                 </div>
                             ) : null}
                             <i
                                 className=\"icon-warning-sign\"
                                 style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                                 onClick={() => {
                                     openInsightAccessModal(insightsMap[tile.insightId]);
                                 }}
                                 dashboard-no-select=\"\">
                                 <i className=\"icon-warning-sign\" style=\"margin-right: 7px; cursor: pointer;\">
                                     <i className=\"icon-external-link\">
                                         <i className=\"icon-copy\">
                                             <i className=\"icon-trash\" dashboard-no-select=\"\">
                                                 <i
                                                     className=\"icon-warning-sign text-error\"
                                                     toggle=\"tooltip\"
                                                     title=\"Insight not accessible to dashboard users\"
                                                     placement=\"bottom\"
                                                     container=\"body\"
                                                     style=\"font-size: 40px\">
                                                     <i
                                                         className=\"icon-warning-sign text-error\"
                                                         toggle=\"tooltip\"
                                                         title=\"Foreign source not exposed to this project\"
                                                         placement=\"bottom\"
                                                         container=\"body\"
                                                         style=\"font-size: 40px\">
                                                         <span id=\"dashboard-export-toolbox-anchor\" />
                                                     </i>
                                                 </i>
                                             </i>
                                         </i>
                                     </i>
                                 </i>
                             </i>
                         </i>
                     </i>
                 </div>
                 <i className=\"icon-plus-sign inline-left-panel-plus\">
                     <i className=\"icon-plus-sign inline-page-plus\">
                         <i
                             className=\"icon-warning-sign\"
                             style=\"color: #c09853; margin-right: 7px; cursor: pointer;\"
                             onClick={() => {
                                 openInsightAccessModal(insightsMap[tile.insightId]);
                             }}
                             dashboard-no-select=\"\">
                             <i className=\"icon-warning-sign\" style=\"margin-right: 7px; cursor: pointer;\">
                                 <i className=\"icon-external-link\">
                                     <i className=\"icon-copy\">
                                         <i className=\"icon-trash\" dashboard-no-select=\"\">
                                             <i
                                                 className=\"icon-warning-sign text-error\"
                                                 toggle=\"tooltip\"
                                                 title=\"Insight not accessible to dashboard users\"
                                                 placement=\"bottom\"
                                                 container=\"body\"
                                                 style=\"font-size: 40px\">
                                                 <i
                                                     className=\"icon-warning-sign text-error\"
                                                     toggle=\"tooltip\"
                                                     title=\"Foreign source not exposed to this project\"
                                                     placement=\"bottom\"
                                                     container=\"body\"
                                                     style=\"font-size: 40px\"
                                                 />
                                             </i>
                                         </i>
                                     </i>
                                 </i>
                             </i>
                         </i>
                     </i>
                 </i>
             </div>
         );
     };
     
     export default TestComponent;