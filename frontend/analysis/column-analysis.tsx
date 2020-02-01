import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" className=\"feature-section-with-title\">
    +             {data ? (
    +                 <div className=\"row-fluid\">
    +                     <h5 className=\"feature-section-title main-zone-element\">分布</h5>
    +                     {isNumeric ? (
    +                         <div className=\"column-analysis number-analysis\">
    +                             <div className=\"main-zone-element statistics-table\">
    +                                 <table className=\"table\">
    +                                     <tbody>
    +                                         <tr>
    +                                             <th>最小 </th>
    +                                             <td>{isDate ? date(data.min) : nicePrecision(data.min, 5)}</td>
    +                                             <th>Mean </th>
    +                                             <td>{isDate ? date(data.mean) : nicePrecision(data.mean, 5)}</td>
    +                                             <th>不同值</th>
    +                                             <td>{data.cardinality}</td>
    +                                             <th>空数据格</th>
    +                                             {data.nbEmpty.toFixed ? (
    +                                                 <td>
    +                                                     {data.nbEmpty} ({number(data.pcEmpty, 1)}%)
    +                                                 </td>
    +                                             ) : null}
    +                                             {!data.nbEmpty.toFixed ? <td>{number(data.pcEmpty, 1) + '%'}</td> : null}
    +                                         </tr>
    +                                         <tr>
    +                                             <th>最大值 </th>
    +                                             <td>{isDate ? date(data.max) : nicePrecision(data.max, 5)}</td>
    +                                             <th>StdDev</th>
    +                                             <td>
    +                                                 {isDate
    +                                                     ? friendlyDurationShort(data.stddev)
    +                                                     : nicePrecision(data.stddev, 5)}
    +                                             </td>
    +                                             <th>模式 </th>
    +                                             <td>{isDate ? date(data.mode) : nicePrecision(data.mode, 5)}</td>
    +                                             <th>无效数据格</th>
    +                                             {data.nbNOK.toFixed ? (
    +                                                 <td>
    +                                                     {data.nbNOK} ({number(data.pcNOK, 1)}%)
    +                                                 </td>
    +                                             ) : null}
    +                                             {!data.nbNOK.toFixed ? <td>{number(data.pcNOK, 1) + '%'}</td> : null}
    +                                         </tr>
    +                                         <tr>
    +                                             <th />
    +                                             <td />
    +                                             <th>中位数</th>
    +                                             <td>{isDate ? date(data.median) : nicePrecision(data.median, 5)}</td>
    +                                             <th />
    +                                             <td />
    +                                             <th />
    +                                             <td />
    +                                         </tr>
    +                                     </tbody>
    +                                 </table>
    +                             </div>
    + 
    +                             <div className=\"span6\">
    +                                 <BoxPlot data=\"data\" height=\"25\" />
    +                                 <histogram data=\"data\" height=\"100\" is-date=\"isDate\" style=\"margin-top: 10px\" />
    +                             </div>
    +                         </div>
    +                     ) : null}
    +                     {!isNumeric ? (
    +                         <div className=\"column-analysis text-analysis\">
    +                             <div className=\"span3 re-select main-zone-element\">
    +                                 <strong>{data.totalNbValues}</strong> 不同值,<br /> with
    +                                 {data.nbEmpty.toFixed ? (
    +                                     <span>
    +                                         <strong>
    +                                             {data.nbEmpty}
    +                                             空数据格 ({number(data.pcEmpty, 1)}%)
    +                                         </strong>
    +                                     </span>
    +                                 ) : null}
    +                                 <strong>
    +                                     {data.nbEmpty}
    +                                     {!data.nbEmpty.toFixed ? (
    +                                         <span>
    +                                             <strong>
    +                                                 {number(data.pcEmpty, 1) + '%'}
    +                                                 空数据格
    +                                             </strong>
    +                                         </span>
    +                                     ) : null}
    +                                     <strong>{number(data.pcEmpty, 1) + '%'}</strong>
    +                                 </strong>
    +                             </div>
    +                             <strong>
    +                                 {data.nbEmpty}
    +                                 <strong>
    +                                     {number(data.pcEmpty, 1) + '%'}
    +                                     {!asList ? (
    +                                         <div className=\"span7\">
    +                                             <BarChart data=\"data\" height=\"180\" count=\"count\" />
    +                                         </div>
    +                                     ) : null}
    +                                     {asList ? (
    +                                         <div className=\"span7\">
    +                                             <ul>
    +                                                 {data.values.map((v, index: number) => {
    +                                                     return <li key={`item-${index}`}>{v}</li>;
    +                                                 })}
    +                                                 {data.totalNbValues > data.values.length ? <li>…</li> : null}
    +                                             </ul>
    +                                         </div>
    +                                     ) : null}
    +                                 </strong>
    +                             </strong>
    +                         </div>
    +                     ) : null}
    +                     <strong>
    +                         {data.nbEmpty}
    +                         <strong>{number(data.pcEmpty, 1) + '%'}</strong>
    +                     </strong>
    +                 </div>
    +             ) : null}
    +             <strong>
    +                 {data.nbEmpty}
    +                 <strong>
    +                     {number(data.pcEmpty, 1) + '%'}
    +                     {data === null && isNumeric ? <p className=\"alert alert-warning\">无法在此功能中找到数字.</p> : null}
    +                 </strong>
    +             </strong>
    +         </div>,
    +         <strong key=\"child-1\">
    +             {data.nbEmpty}
    +             <strong>{number(data.pcEmpty, 1) + '%'}</strong>
    +         </strong>
    +     ];
    + };
    + 
    + export default TestComponent;