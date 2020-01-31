import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <form className=\"dkuform-modal-wrapper\" global-keydown=\"{'esc': 'cancel();' }\">
    +                 {/* Eat the focus so that a dangerous button don't get it */}
    +                 <input type=\"hidden\" autoFocus={true} />
    +                 <DkuModalHeader modal-title={title} modal-close=\"cancel\" />
    +                 <div className=\"modal-body tight-body\">
    +                     {disclaimer ? (
    +                         <div className=\"disclaimer\">
    +                             <p dangerouslySetInnerHTML={{__html: 'disclaimer'}} />
    +                         </div>
    +                     ) : null}
    +                     <p dangerouslySetInnerHTML={{__html: 'text'}} />
    +                 </div>
    +                 <div className=\"modal-footer modal-footer-std-buttons\">
    +                     {/* Since there is no input with focus, button type='submit' is not enough, we need a global
    +             handler */}
    +                     {positive ? (
    +                         <div className=\"pull-right\" global-keydown=\"{'enter': 'confirm();' }\">
    +                             <button
    +                                 type=\"button\"
    +                                 className=\"btn btn-default btn-lg\"
    +                                 onClick={() => {
    +                                     cancel();
    +                                 }}>
    +                                 取消
    +                             </button>
    +                             <button
    +                                 type=\"submit\"
    +                                 className=\"btn btn-lg btn-primary\"
    +                                 onClick={() => {
    +                                     confirm();
    +                                 }}>
    +                                 确定
    +                             </button>
    +                         </div>
    +                     ) : null}
    +                     {!positive ? (
    +                         <div className=\"pull-right\">
    +                             {/* Dangerous modals don't validate on enter*/}
    +                             <button
    +                                 type=\"button\"
    +                                 className=\"btn btn-default btn-lg\"
    +                                 onClick={() => {
    +                                     cancel();
    +                                 }}>
    +                                 取消
    +                             </button>
    +                             <button
    +                                 type=\"button\"
    +                                 className=\"btn btn-danger btn-lg\"
    +                                 onClick={() => {
    +                                     confirm();
    +                                 }}>
    +                                 确定
    +                             </button>
    +                         </div>
    +                     ) : null}
    +                 </div>
    +             </form>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;