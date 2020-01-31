import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <div dku-modal-header=\"\" modal-title=\"删除工程发生错误\" modal-class=\"has-border\">
    +                 <form className=\"dkuform-modal-wrapper\">
    +                     <div className=\"modal-body\">
    +                         <div className=\"row-fluid\">
    +                             <div block-api-error=\"\" />
    +                             {beforeDeletion ? <p>此工程不能被删除:</p> : null}
    +                             {!beforeDeletion ? <p>在删除过程中发生了一些错误:</p> : null}
    +                             <ul>
    +                                 {results.map((result, index: number) => {
    +                                     return (
    +                                         <li key={`item-${index}`}>
    +                                             <span className=\"text-error\" dku-inline-popover=\"\" container=\"body\">
    +                                                 <label> {result.msg} </label>
    +                                                 <content title=\"Details\"> {result.cause} </content>
    +                                             </span>
    +                                         </li>
    +                                     );
    +                                 })}
    +                             </ul>
    +                         </div>
    +                     </div>
    +                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                         <div className=\"pull-right\">
    +                             <button
    +                                 type=\"submit\"
    +                                 className=\"btn btn-primary\"
    +                                 onClick={() => {
    +                                     dismiss();
    +                                 }}>
    +                                 好的
    +                             </button>
    +                         </div>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;