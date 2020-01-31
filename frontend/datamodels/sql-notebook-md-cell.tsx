import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         !cell.$localState.unfolded ? (
    +             <div
    +                 key=\"child-0\"
    +                 className=\"cell-header folded\"
    +                 onClick={() => {
    +                     toggleCell();
    +                 }}
    +                 ui-keydown=\"{'right':'toggleCell()'}\">
    +                 <span className=\"cell-title\">
    +                     <i className=\"icon-caret-right\">{cell.name || 'Comment'}</i>
    +                 </span>
    +                 <i className=\"icon-caret-right\" />
    +             </div>
    +         ) : null,
    +         <i key=\"child-1\" className=\"icon-caret-right\">
    +             <form className=\"dkuform-std-vertical\" style=\"margin:0\">
    +                 {cell.$localState.unfolded ? (
    +                     <div
    +                         className=\"cell-header unfolded\"
    +                         onClick={() => {
    +                             toggleCell();
    +                         }}>
    +                         <div className=\"cell-tools pull-right\" stop-propagation=\"\">
    +                             <a
    +                                 onClick={() => {
    +                                     duplicateCell($index);
    +                                 }}
    +                                 className=\"cell-tool link-std\">
    +                                 <i className=\"icon-copy\" />
    +                             </a>
    +                             <i className=\"icon-copy\">
    +                                 <a
    +                                     onClick={() => {
    +                                         removeCell($index);
    +                                     }}
    +                                     className=\"cell-tool link-std\">
    +                                     <i className=\"icon-trash\" />
    +                                 </a>
    +                                 <i className=\"icon-trash\" />
    +                             </i>
    +                         </div>
    +                         <i className=\"icon-copy\">
    +                             <i className=\"icon-trash\">
    +                                 <span className=\"cell-title\">
    +                                     <i className=\"icon-caret-down\">
    +                                         <input
    +                                             type=\"text\"
    +                                             value={cell.name}
    +                                             placeholder=\"点击命名此备注...\"
    +                                             className=\"in-place-edit\"
    +                                             stop-propagation=\"\"
    +                                         />
    +                                     </i>
    +                                 </span>
    +                                 <i className=\"icon-caret-down\" />
    +                             </i>
    +                         </i>
    +                     </div>
    +                 ) : null}
    +                 <i className=\"icon-copy\">
    +                     <i className=\"icon-trash\">
    +                         <i className=\"icon-caret-down\">
    +                             {cell.$localState.unfolded ? (
    +                                 <div className=\"cell-body\">
    +                                     <div className=\"cell-code secondary-scroll\" style=\"position:relative\">
    +                                         {cell.$tmpState.mdCellEditModeOn ? (
    +                                             <div ui-keydown=\"{'ctrl-enter meta-enter':'ok()', 'esc': 'cell.$tmpState.mdCellEditModeOn = false'}\">
    +                                                 <textarea value={cell.$localState.tmpCode} />
    +                                                 <div className=\"actions\">
    +                                                     <a
    +                                                         className=\"btn btn-success\"
    +                                                         onClick={() => {
    +                                                             ok();
    +                                                         }}>
    +                                                         {' '}
    +                                                         完成{' '}
    +                                                     </a>
    +                                                     <a
    +                                                         className=\"btn btn-default\"
    +                                                         onClick={() => {
    +                                                             cell.$tmpState.mdCellEditModeOn = false;
    +                                                         }}>
    +                                                         {' '}
    +                                                         取消{' '}
    +                                                     </a>
    +                                                 </div>
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                     {!cell.$tmpState.mdCellEditModeOn && cell.code.trim() ? (
    +                                         <div
    +                                             className=\"rendered\"
    +                                             from-markdown=\"cell.code\"
    +                                             onClick={() => {
    +                                                 cell.$tmpState.mdCellEditModeOn = true;
    +                                             }}
    +                                             title=\"click to edit\"
    +                                         />
    +                                     ) : null}
    +                                     {!cell.$tmpState.mdCellEditModeOn && !cell.code.trim() ? (
    +                                         <div className=\"rendered\" full-click=\"\">
    +                                             <a
    +                                                 onClick={() => {
    +                                                     cell.$tmpState.mdCellEditModeOn = true;
    +                                                 }}
    +                                                 main-click=\"\">
    +                                                 点击创建一个备注 (文本或 Markdown)
    +                                             </a>
    +                                         </div>
    +                                     ) : null}
    +                                 </div>
    +                             ) : null}
    +                         </i>
    +                     </i>
    +                 </i>
    +             </form>
    +         </i>
    +     ];
    + };
    + 
    + export default TestComponent;