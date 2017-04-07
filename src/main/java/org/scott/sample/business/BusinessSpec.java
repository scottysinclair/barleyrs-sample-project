package org.scott.sample.business;

import static scott.barleydb.api.specification.CoreSpec.mandatoryRefersTo;
import static scott.barleydb.api.specification.CoreSpec.optionallyRefersTo;
import static scott.barleydb.api.specification.CoreSpec.ownsMany;
import static scott.barleydb.api.specification.CoreSpec.refersToMany;

import scott.barleydb.api.specification.NodeSpec;
import scott.barleydb.build.specification.staticspec.CommonDefaultsPlatformSpec;
import scott.barleydb.build.specification.staticspec.Entity;

public class BusinessSpec extends CommonDefaultsPlatformSpec {

    public BusinessSpec() {
        super("org.scott.sample.business");
    }

    public interface TopLevelModel {
       NodeSpec id = longPrimaryKey();

       NodeSpec modifiedAt = optimisticLock();
    }


    /**
     * An employee in the company
     * @author scott
     *
     */
    @Entity("BS_EMPLOYEE")
    public static class Employee implements TopLevelModel {

        public static NodeSpec name = name();

        public static NodeSpec ssn = mandatoryVarchar50();

        public static NodeSpec dateOfBirth = mandatoryDate();

        public static final NodeSpec department = mandatoryRefersTo(Department.class);

        /**
         *  a relation of many lines of business over the join table entity 'LineOfBusinessEmployees'
         */
        public static final NodeSpec linesOfBusiness = refersToMany(LineOfBusinessEmployees.class, LineOfBusinessEmployees.employee, LineOfBusinessEmployees.lineOfBusiness);
    }


    /**
     * A department in the company
     * @author scott
     *
     */
    @Entity("BS_DEPARTMENT")
    public static class Department implements TopLevelModel {

        public static NodeSpec name = name();

        public static final NodeSpec departmentHead = mandatoryRefersTo(Employee.class);

        public static final NodeSpec employees = refersToMany(Employee.class, Employee.department);

        public static final NodeSpec country = mandatoryRefersTo(Country.class);
    }

    /**
     * A country
     * @author scott
     *
     */
    @Entity("BS_COUNTRY")
    public static class Country implements TopLevelModel {

        public static NodeSpec name = name();

    }

    /**
     * A line of business
     */
    @Entity("BS_LOB")
    public static class LineOfBusiness implements TopLevelModel {

        public static NodeSpec name = name();

        public static final NodeSpec parent = optionallyRefersTo(LineOfBusiness.class);

        public static final NodeSpec children = ownsMany(LineOfBusiness.class, LineOfBusiness.parent);

        /**
         *  a relation of many employees over the join table entity 'LineOfBusinessEmployees'
         */
        public static final NodeSpec employees = refersToMany(LineOfBusinessEmployees.class, LineOfBusinessEmployees.lineOfBusiness, LineOfBusinessEmployees.employee);
    }

    /**
     * A join table between employees and line of business
     *
     * @author scott
     *
     */
    @Entity("BS_LOB_EMPLOYEE")
    public static class LineOfBusinessEmployees  {

        public static final NodeSpec id = longPrimaryKey();

        public static final NodeSpec employee = mandatoryRefersTo(Employee.class);

        public static final NodeSpec lineOfBusiness = mandatoryRefersTo(LineOfBusiness.class);
    }


}
