import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return cell.$localState.selected ? (
    +         <div>
    +             <form className=\"dkuform-std-vertical\" style=\"margin:0\">
    +                 <div className=\"cell-header unfolded\">
    +                     <span className=\"cell-title\">
    +                         <input
    +                             type=\"text\"
    +                             value={cell.name}
    +                             placeholder=\"Cell name\"
    +                             className=\"in-place-edit\"
    +                             stop-propagation=\"\"
    +                         />
    +                     </span>
    + 
    +                     <div className=\"cell-tools pull-right\">
    +                         <a
    +                             onClick={() => {
    +                                 duplicateCell($index);
    +                             }}
    +                             className=\"cell-tool link-std\">
    +                             <i className=\"icon-copy\" />
    +                         </a>
    +                         <i className=\"icon-copy\">
    +                             <a
    +                                 onClick={() => {
    +                                     removeCell($index);
    +                                 }}
    +                                 className=\"cell-tool link-std\">
    +                                 <i className=\"icon-trash\" />
    +                             </a>
    +                             <i className=\"icon-trash\" />
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-copy\">
    +                         <i className=\"icon-trash\" />
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-copy\">
    +                     <i className=\"icon-trash\">
    +                         <div className=\"cell-body\">
    +                             {cell.$tmpState.mdCellEditModeOn ? (
    +                                 <div ui-keydown=\"{'ctrl-enter meta-enter':'ok()', 'esc': 'cell.$tmpState.mdCellEditModeOn = false'}\">
    +                                     <textarea value={cell.$localState.tmpCode} style=\"margin-left: 20px\" />
    +                                     <div className=\"actions\">
    +                                         <a
    +                                             className=\"btn btn-success\"
    +                                             onClick={() => {
    +                                                 ok();
    +                                             }}>
    +                                             {' '}
    +                                             完成{' '}
    +                                         </a>
    +                                         <a
    +                                             className=\"btn btn-default\"
    +                                             onClick={() => {
    +                                                 cell.$tmpState.mdCellEditModeOn = false;
    +                                             }}>
    +                                             {' '}
    +                                             取消{' '}
    +                                         </a>
    +                                     </div>
    +                                 </div>
    +                             ) : null}
    + 
    +                             {!cell.$tmpState.mdCellEditModeOn && cell.code.trim() ? (
    +                                 <div
    +                                     className=\"rendered\"
    +                                     style=\"margin-left: 14px\"
    +                                     from-markdown=\"cell.code\"
    +                                     onClick={() => {
    +                                         cell.$tmpState.mdCellEditModeOn = true;
    +                                     }}
    +                                     title=\"click to edit\"
    +                                 />
    +                             ) : null}
    +                             {!cell.$tmpState.mdCellEditModeOn && !cell.code.trim() ? (
    +                                 <div className=\"rendered\" style=\"margin-left: 14px\" full-click=\"\">
    +                                     <a
    +                                         onClick={() => {
    +                                             cell.$tmpState.mdCellEditModeOn = true;
    +                                         }}
    +                                         main-click=\"\">
    +                                         点击创建一个备注 (文本或Markdown)
    +                                     </a>
    +                                 </div>
    +                             ) : null}
    +                         </div>
    +                     </i>
    +                 </i>
    +             </form>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;