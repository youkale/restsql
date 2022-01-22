# RESTSql

## 通过编写sql就能实现数据操作

## Usage
通过执行编写在`resources/queries.sql`的Sql,结合API就能访问到对应编写的查询.

API: `http://localhost:3000/api/:method-name?a=1&b=2` 
- `:method-name` 对应SQL里 `:name`
- `a=1&b=2` 对应 `:a` `:b`

如下: 对应的API为 `/api/character-by-id?id=1` (SQL[详细文档](https://www.hugsql.org/using-hugsql/select))
```sql
-- A ":result" value of ":1" specifies a single record
-- (as a hashmap) will be returned
-- :name character-by-id :? :1
-- :doc Get character by id
select * from characters
where id = :id
```

## Package
因为是clojure编写，需要有java环境，同时安装安装[lein](https://leiningen.org) 
```clojure
;; 如果是第一次运行，需要下载依赖jar包.
lein clean && lein uberjar
```
打包
- 目录 `./target/restsql.jar`
- 启动: `java -jar ./target/restsql.jar`

## 数据支持

修改 `project.clj` 下的 `:dependencies`, 同时修改 `restsql.db` 下 `datasource-options` [配置](https://cljdoc.org/d/hikari-cp/hikari-cp/2.13.0/doc/readme)

## License

Copyright © 2022 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
