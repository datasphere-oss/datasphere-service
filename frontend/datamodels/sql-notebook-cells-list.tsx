import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"sql-notebook-cells-list selectable-list\" remaining-height=\"\" style=\"background: #fafafa\">
    +             {cells && !cells.length ? (
    +                 <div style=\"text-align: center; padding-top: 40px;color: #999;\">
    +                     <h4>在脚本中没有查询</h4>
    +                 </div>
    +             ) : null}
    + 
    +             {cells.length ? (
    +                 <div className=\"header\">
    +                     {/* {{cells.length}} cells */}
    +                     <div className=\"std-list-search-box\">
    +                         <span className=\"add-on\">
    +                             {' '}
    +                             <i className=\"icon-dku-search\" />
    +                         </span>
    +                         <input
    +                             type=\"search\"
    +                             value={notebookLocalState.cellsQuery}
    +                             style=\"width: 256px;\"
    +                             placeholder=\"Filter...\"
    +                             ui-keydown=\"{'enter':'filterCells()'}\"
    +                         />
    +                     </div>
    +                 </div>
    +             ) : null}
    + 
    +             {cells && cells.length && filteredCells != cells.length ? (
    +                 <div style=\"padding: 2px 5px; color: #999; font-style: italic;\">
    +                     显示 {filteredCells} 的 {cells.length} 数据格
    +                 </div>
    +             ) : null}
    + 
    +             <div
    +                 className=\"list-body oa\"
    +                 style={`height:${remainingHeight - 60}px`}
    +                 ui-sortable=\"{axis:'y', cursor: 'move', handle: '.handle'}\"
    +                 value={cells}>
    +                 {!cell.$tmpState.filteredOut
    +                     ? cells.map((cell, index: number) => {
    +                           return (
    +                               <div key={`item-${index}`}>
    +                                   {!cell.$localState.selected ? (
    +                                       <div
    +                                           className=\"{error: !cell.$tmpState.runningQuery &amp;&amp; cell.$tmpState.results &amp;&amp; !cell.$tmpState.results.success,
    +                             success: !cell.$tmpState.runningQuery &amp;&amp; cell.$tmpState.results.success
    +                     }\"
    +                                           full-click=\"\">
    +                                           <span className=\"handle\">⋮</span>
    +                                           {!cell.$localState.selected ? (
    +                                               <a
    +                                                   onClick={() => {
    +                                                       selectCellAndScroll($index);
    +                                                   }}
    +                                                   className=\"flex\"
    +                                                   main-click=\"\">
    +                                                   {cell.type == 'QUERY' ? (
    +                                                       <i className=\"icon-code\">
    +                                                           {cell.type == 'MARKDOWN' ? (
    +                                                               <i className=\"icon-comment-alt\">
    +                                                                   {cell.name ||
    +                                                                   cell.$localState.query.sql.trim() ||
    +                                                                   cell.type == 'QUERY'
    +                                                                       ? 'Empty query'
    +                                                                       : 'Comment'}
    +                                                               </i>
    +                                                           ) : null}
    +                                                       </i>
    +                                                   ) : null}
    +                                               </a>
    +                                           ) : null}
    +                                           {cell.type == 'QUERY' ? (
    +                                               <i className=\"icon-code\">
    +                                                   {cell.type == 'MARKDOWN' ? (
    +                                                       <i className=\"icon-comment-alt\">
    +                                                           {cell.$tmpState.runningQuery ? (
    +                                                               <span className=\"noflex\">
    +                                                                   <i className=\"icon-spinner icon-spin\" />
    +                                                               </span>
    +                                                           ) : null}
    +                                                           <i className=\"icon-spinner icon-spin\" />
    +                                                       </i>
    +                                                   ) : null}
    +                                               </i>
    +                                           ) : null}
    +                                       </div>
    +                                   ) : null}
    +                                   {cell.type == 'QUERY' ? (
    +                                       <i className=\"icon-code\">
    +                                           {cell.type == 'MARKDOWN' ? (
    +                                               <i className=\"icon-comment-alt\">
    +                                                   <i className=\"icon-spinner icon-spin\">
    +                                                       {cell.$localState.selected ? (
    +                                                           <div className=\"item selectable-item horizontal-flex selected\">
    +                                                               <span className=\"handle\">⋮</span>
    +                                                               {cell.$localState.selected ? (
    +                                                                   <span className=\"flex\">
    +                                                                       {cell.type == 'QUERY' ? (
    +                                                                           <i className=\"icon-code\">
    +                                                                               {cell.type == 'MARKDOWN' ? (
    +                                                                                   <i className=\"icon-comment-alt\">
    +                                                                                       {cell.name ||
    +                                                                                       cell.$localState.query.sql.trim() ||
    +                                                                                       cell.type == 'QUERY'
    +                                                                                           ? 'Empty query'
    +                                                                                           : 'Comment'}
    +                                                                                   </i>
    +                                                                               ) : null}
    +                                                                           </i>
    +                                                                       ) : null}
    +                                                                   </span>
    +                                                               ) : null}
    +                                                               {cell.type == 'QUERY' ? (
    +                                                                   <i className=\"icon-code\">
    +                                                                       {cell.type == 'MARKDOWN' ? (
    +                                                                           <i className=\"icon-comment-alt\">
    +                                                                               {cell.$tmpState.runningQuery ? (
    +                                                                                   <span className=\"noflex\">
    +                                                                                       <i className=\"icon-spinner icon-spin\" />
    +                                                                                   </span>
    +                                                                               ) : null}
    +                                                                               <i className=\"icon-spinner icon-spin\" />
    +                                                                           </i>
    +                                                                       ) : null}
    +                                                                   </i>
    +                                                               ) : null}
    +                                                           </div>
    +                                                       ) : null}
    +                                                       {cell.type == 'QUERY' ? (
    +                                                           <i className=\"icon-code\">
    +                                                               {cell.type == 'MARKDOWN' ? (
    +                                                                   <i className=\"icon-comment-alt\">
    +                                                                       <i className=\"icon-spinner icon-spin\" />
    +                                                                   </i>
    +                                                               ) : null}
    +                                                           </i>
    +                                                       ) : null}
    +                                                   </i>
    +                                               </i>
    +                                           ) : null}
    +                                       </i>
    +                                   ) : null}
    +                               </div>
    +                           );
    +                       })
    +                     : null}
    +                 {cell.type == 'MARKDOWN' ? (
    +                     <i className=\"icon-comment-alt\">
    +                         <i className=\"icon-spinner icon-spin\">
    +                             {cell.type == 'QUERY' ? (
    +                                 <i className=\"icon-code\">
    +                                     {cell.type == 'MARKDOWN' ? (
    +                                         <i className=\"icon-comment-alt\">
    +                                             <i className=\"icon-spinner icon-spin\" />
    +                                         </i>
    +                                     ) : null}
    +                                 </i>
    +                             ) : null}
    +                         </i>
    +                     </i>
    +                 ) : null}
    +             </div>
    +             {cell.type == 'MARKDOWN' ? (
    +                 <i className=\"icon-comment-alt\">
    +                     <i className=\"icon-spinner icon-spin\">
    +                         {cell.type == 'QUERY' ? (
    +                             <i className=\"icon-code\">
    +                                 {cell.type == 'MARKDOWN' ? (
    +                                     <i className=\"icon-comment-alt\">
    +                                         <i className=\"icon-spinner icon-spin\" />
    +                                     </i>
    +                                 ) : null}
    +                             </i>
    +                         ) : null}
    +                     </i>
    +                 </i>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;