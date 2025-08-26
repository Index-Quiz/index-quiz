package com.example.indexquiz.dependency;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("NonAsciiCharacters")
public class ConventionTest {

    @Nested
    class DependencyRuleTest {

        //규칙1. Adapter는 Port 명세를 구현한다
        //규칙2. Application.Service 는 UseCase 명세를 구현한다
        //규칙3. Domain <- Application <-Adapter 의존성을 따라야 한다
        // 3-1) Domain은 Application, Adaapter 중 어떤 의존성도 가지면 안된다
        // 3-2) Application.service는 추상화된 명세(UserCase, Port)하고만 소통한다
        // 3-3) Application.service는 명세의 구현체(Adapter)를 모른다
        // 3-4) JpaEntity는 persistence 패키지 내에서만 참조되어야 한다
        // 3-5) Controller는 UserCase를 통해 도메인과 소통한다

        @Test
        void 아웃패키지의_어댑터는_아웃포트를_구현하여야_한다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            ArchRule rule = ArchRuleDefinition.classes()
                    .that()
                    .resideInAPackage("..out..")
                    .and()
                    .haveSimpleNameEndingWith("Adapter")
                    .should()
                    .dependOnClassesThat()
                    .haveSimpleNameEndingWith("Port");

            rule.check(importedClasses);
        }

        @Test
        void 어플리케이션_서비스는_하나이상의_유즈케이스를_구현하여야_한다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            ArchRule rule = ArchRuleDefinition.classes()
                    .that()
                    .resideInAPackage("..application.service..")
                    .and()
                    .areAnnotatedWith(Service.class)
                    .should()
                    .dependOnClassesThat()
                    .haveSimpleNameEndingWith("UseCase");

            rule.check(importedClasses);
        }

        @Test
        void 도메인은_바깥계층과의_의존성을_지니면_안된다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            ArchRule rule = ArchRuleDefinition.noClasses()
                    .that()
                    .resideInAPackage("..domain..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage("..adapter..", "..application..");

            rule.check(importedClasses);
        }

        @Test
        void 어플리케이션_서비스는_포트_유즈케이스을_통해_도메인을_소통한다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            ArchRule rule = ArchRuleDefinition.classes()
                    .that()
                    .resideInAPackage("..application.service..")
                    .should()
                    .onlyHaveDependentClassesThat()
                    .resideInAnyPackage("..domain..", "..application.port..");

            rule.check(importedClasses);
        }

        @Test
        void 어플리케이션_서비스는_소통_명세의_구현체인_어뎁터를_모른다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            ArchRule rule = ArchRuleDefinition.noClasses()
                    .that()
                    .resideInAPackage("..application.service..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage("..adapter..");

            rule.check(importedClasses);
        }

        @Test
        void jpa엔티티는_어댑터_패키지_내에서만_참조되어야_한다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            ArchRule rule  = ArchRuleDefinition.classes()
                    .that()
                    .areAnnotatedWith(Entity.class)
                    .should()
                    .onlyBeAccessed()
                    .byAnyPackage("..adapter.out..");

            rule.check(importedClasses);
        }

        @Test
        void 컨트롤러는_유즈케이스를_통해_도메인과_소통한다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            ArchRule rule  = ArchRuleDefinition.classes()
                    .that()
                    .areAnnotatedWith(RestController.class)
                    .should()
                    .onlyHaveDependentClassesThat()
                    .resideInAPackage("..port.in..");

            rule.check(importedClasses);
        }
    }

    @Nested
    class NamingRuleTest {
        //규칙1. UseCase는 *UserCase로 이름이 끝나야 한다
        //규칙2. Port는 *Port로 이름이 끝나야 한다
        //규칙3. Service는 *Service로 이름이 끝나야 한다
        //규칙4. Controller는 *Controller로 이름이 끝나야 한다
    }

    @Nested
    class CycleTest {

        @Test
        void 순환참조가_존재해서는_안된다() {
            JavaClasses importedClasses = new ClassFileImporter()
                    .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                    .importPackages("com.example.indexquiz");

            SliceRule sliceRule = SlicesRuleDefinition.slices()
                    .matching("com.example.indexquiz.(*)..")
                    .should()
                    .beFreeOfCycles();

            sliceRule.check(importedClasses);
        }

    }

    @Nested
    class AnnotationTest {

        //규칙1. Controller는 @RestController가 붙어있어야 한다
        //규칙2. Service는 @Service가 붙어있어야 한다.

    }
}
