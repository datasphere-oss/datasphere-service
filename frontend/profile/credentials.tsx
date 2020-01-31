import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"boxed-next-to-sidebar h100\">
    +             <div className=\"h100 vertical-flex\">
    +                 <div block-api-error=\"\">
    +                     <h1 className=\"page-title\">个人连接认证</h1>
    + 
    +                     {credentials.credentials.length == 0 ? (
    +                         <div className=\"placeholder-block\" style=\"margin-top: 35px;\">
    +                             没有连接需要个人凭据
    +                         </div>
    +                     ) : null}
    + 
    +                     {credentials.credentials.length != 0 ? (
    +                         <div>
    +                             <div className=\"alert alert-warning\">DSS 管理员可以解密这些认证</div>
    + 
    +                             <table sort-table=\"\" className=\"table table-striped table-hover\" style=\"width:100%\">
    +                                 <thead>
    +                                     <tr>
    +                                         <th sort-col=\"connectionType\">类型</th>
    +                                         <th sort-col=\"connection\">连接</th>
    +                                         <th sort-col=\"user\">用户</th>
    +                                         <th />
    +                                     </tr>
    +                                 </thead>
    +                                 <tbody>
    +                                     {credentials.credentials.map((credential, index: number) => {
    +                                         return (
    +                                             <tr key={`item-${index}`}>
    +                                                 <td>{credential.connectionType}</td>
    +                                                 <td>{cleanConnectionName(credential.connection)}</td>
    +                                                 <td>
    +                                                     {credential.user ? (
    +                                                         <span>
    +                                                             {credential.user}
    + 
    +                                                             {!credential.user ? (
    +                                                                 <span>
    +                                                                     <em>无认证输入</em>
    +                                                                 </span>
    +                                                             ) : null}
    +                                                         </span>
    +                                                     ) : null}
    +                                                 </td>
    +                                                 <td style=\"font-size: 15px\">
    +                                                     <a
    +                                                         className=\"link-std text-error\"
    +                                                         onClick={() => {
    +                                                             editCredential(credential);
    +                                                         }}>
    +                                                         {' '}
    +                                                         <i className=\"icon-edit\" />
    +                                                     </a>
    +                                                     <i className=\"icon-edit\">
    +                                                         <span className=\"text-error\">
    +                                                             <a
    +                                                                 className=\"link-std text-error\"
    +                                                                 onClick={() => {
    +                                                                     deleteCredential(credential.connection);
    +                                                                 }}>
    +                                                                 {' '}
    +                                                                 <i className=\"icon-trash\" />
    +                                                             </a>
    +                                                         </span>
    +                                                         <i className=\"icon-trash\" />
    +                                                     </i>
    +                                                 </td>
    +                                             </tr>
    +                                         );
    +                                     })}
    +                                 </tbody>
    +                             </table>
    +                         </div>
    +                     ) : null}
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;