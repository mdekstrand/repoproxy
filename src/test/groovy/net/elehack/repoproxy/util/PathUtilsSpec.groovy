package net.elehack.repoproxy.util

import spock.lang.Specification

class PathUtilsSpec extends Specification {
    def "tokenize path"() {
        expect:
        PathUtils.tokenize(path) == parsed

        where:
        path       | parsed
        "foo"      | ["foo"]
        "."        | ["."]
        ""         | []
        "foo/"     | ["foo", ""]
        "/foo"     | ["", "foo"]
        "/foo/bar" | ["", "foo", "bar"]
        "/../foo"  | ["", "..", "foo"]
    }

    def "normalize path"() {
        expect:
        PathUtils.normalize(path) == Optional.ofNullable(result)

        where:
        path                   | result
        "foo"                  | "foo"
        "foo/bar"              | "foo/bar"
        "."                    | "."
        "/foo/bar"             | "/foo/bar"
        "foo/bar/"             | "foo/bar/"
        "foo//bar"             | "foo/bar"
        "foo/./bar"            | "foo/bar"
        "foo/../bar"           | "bar"
        "../bar"               | "../bar"
        "bar/.."               | "."
        "./foo"                | "foo"
        ""                     | "."
        "/foo/../../bar"       | null
        "foo/../bar/../wombat" | "wombat"
    }

    def "test absolute"() {
        expect:
        PathUtils.isAbsolute(path) == result

        where:
        path    | result
        "foo"   | false
        "/foo"  | true
        "/"     | true
        "foo/"  | false
        "./foo" | false
    }
}
